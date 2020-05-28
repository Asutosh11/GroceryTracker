package com.highloop.homemaker.data.pojo

class Product {

    constructor(item: String?, quantity: String?, unit: String?, listName: String?) {
        this.item = item
        this.quantity = quantity
        this.unit = unit
        this.listName = listName
    }

    var item: String? = null
    var quantity: String? = null
    var unit: String? = null
    var listName: String? = null
}