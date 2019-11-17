package client.sodium.app.actions

import dataStorage.Value
import client.sodium.core._

case class SActionCreateNewEntity[V <: Value[V]](s: Stream[V])
