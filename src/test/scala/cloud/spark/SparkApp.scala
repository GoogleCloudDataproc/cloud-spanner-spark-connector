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

package cloud.spark

import org.apache.spark.sql.SparkSession

object SparkApp extends App {

  val spark = SparkSession.builder.master("local[*]").getOrCreate()
  println(s"Running Spark ${spark.version}")

  val creds = sys.env.getOrElse("GOOGLE_APPLICATION_CREDENTIALS", "(undefined)")
  println(s"GOOGLE_APPLICATION_CREDENTIALS: $creds")

  val opts = Map(
    "instanceId" -> "dev-instance",
    "databaseId" -> "demo"
  )
  val table = "Account"
  // Until the format is ready, try-finally is to keep sbt shell clean
  // spark.close shuts down all the underlying Spark services
  // and they do not pollute the output
  try {
    val accounts = spark
      .read
      .format("spanner")
      .options(opts)
      .load(table)

    accounts.printSchema
    accounts.show(truncate = false)
  } finally {
    spark.close()
  }
}
