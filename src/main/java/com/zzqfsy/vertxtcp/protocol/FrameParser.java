/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
package com.zzqfsy.vertxtcp.protocol;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

/**
 * Simple LV parser
 *
 * @author Paulo Lopes
 */
public class FrameParser implements Handler<Buffer> {

  private Buffer _buffer;
  private int _offset;

  private final Handler<AsyncResult<Object>> client;

  public FrameParser(Handler<AsyncResult<Object>> client) {
    this.client = client;
  }

  @Override
  public void handle(Buffer buffer) {
    append(buffer);

    int offset;

    while (true) {
      // set a rewind point. if a failure occurs,
      // wait for the next handle()/append() and try again
      offset = _offset;

      // how many bytes are in the buffer
      int remainingBytes = bytesRemaining();

      // at least 4 bytes
      if (remainingBytes < 4) {
        break;
      }

      // what is the length of the message
      int length = _buffer.getInt(_offset);
      _offset += 4;

      if (remainingBytes - 4 >= length) {
        // we have a complete message
        try {
          client.handle(Future.succeededFuture(_buffer.getBytes(_offset, _offset + length)));
        } catch (DecodeException e) {
          // bad json
          client.handle(Future.failedFuture(e));
        }
        _offset += length;
      } else {
        // not enough data: rewind, and wait
        // for the next packet to appear
        _offset = offset;
        break;
      }
    }
  }

  private void append(Buffer newBuffer) {
    if (newBuffer == null) {
      return;
    }

    // first run
    if (_buffer == null) {
      _buffer = newBuffer;

      return;
    }

    // out of data
    if (_offset >= _buffer.length()) {
      _buffer = newBuffer;
      _offset = 0;

      return;
    }

    // very large packet
    if (_offset > 0) {
      _buffer = _buffer.getBuffer(_offset, _buffer.length());
    }
    _buffer.appendBuffer(newBuffer);

    _offset = 0;
  }

  private int bytesRemaining() {
    return (_buffer.length() - _offset) < 0 ? 0 : (_buffer.length() - _offset);
  }
}