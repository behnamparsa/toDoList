package com.techmania.todolistx

import java.io.Serializable

data class TodoItem(
    var title: String,
    var dueAtMillis: Long
) : Serializable

