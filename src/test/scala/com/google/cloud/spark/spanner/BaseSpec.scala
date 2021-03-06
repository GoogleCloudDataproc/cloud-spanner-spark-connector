/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.spark.spanner

import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

abstract class BaseSpec extends FlatSpec
  with Matchers
  with BeforeAndAfterAll {

  override def beforeAll() {
    sys.env.get("GOOGLE_APPLICATION_CREDENTIALS").orElse {
      fail("GOOGLE_APPLICATION_CREDENTIALS env var not defined")
    }
  }

  def withSparkSession(testCode: SparkSession => Any): Unit = {
    val spark = SparkSession.builder.master("local[*]").getOrCreate
    try testCode(spark)
    finally spark.close
  }
}
