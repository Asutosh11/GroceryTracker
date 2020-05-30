package `in`.thoughtleaf.grocerytracker.data.event

data class NewProductAddedEvent(var newProductName: String? = null, var newProductCategory: String? = null)