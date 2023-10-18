package com.sg.retrofitexample.core

interface ViewHolderBinder<T> {
    fun bind(item: T)
}