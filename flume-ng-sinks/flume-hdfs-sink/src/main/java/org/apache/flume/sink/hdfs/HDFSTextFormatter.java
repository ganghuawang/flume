/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.flume.sink.hdfs;

import java.util.Arrays;

import org.apache.flume.Event;
import org.apache.flume.sink.FlumeFormatter;
//import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;


public class HDFSTextFormatter implements FlumeFormatter {

  private Text makeText(Event e) {
    Text textObject = new Text();
    textObject.set(e.getBody(), 0, e.getBody().length);
    return textObject;
  }

  @Override
  public Class<LongWritable> getKeyClass() {
    return LongWritable.class;
  }

  @Override
  public Class<Text> getValueClass() {
    return Text.class;
  }

  @Override
  public Object getKey(Event e) {
    // Write the data to HDFS
    String timestamp = e.getHeaders().get("timestamp");
    long eventStamp;

    if (timestamp == null) {
      eventStamp = System.currentTimeMillis();
    } else {
      eventStamp = Long.valueOf(timestamp);
    }
    LongWritable longObject = new LongWritable(eventStamp);
    return longObject;
  }

  @Override
  public Object getValue(Event e) {
    return makeText(e);
  }

  @Override
  public byte[] getBytes(Event e) {
    Text record = makeText(e);
    record.append("\n".getBytes(), 0, 1);
    byte[] rawBytes = record.getBytes();
    return Arrays.copyOf(rawBytes, record.getLength());
  }

}
