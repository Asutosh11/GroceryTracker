package `in`.thoughtleaf.grocerytracker.data.dao

import android.content.Context
import com.google.gson.Gson
import `in`.thoughtleaf.grocerytracker.data.pojo.AllProducts
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.util.ObjectBoxUtil
import io.objectbox.Box
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.kotlin.boxFor


@Entity
public class ItemsListDAO {

    @Id
    var id: Long = 0

    @Unique
    var itemName: String? = null

    var itemCategory: String? = null

    var itemQuantity: String? = null
    var itemUnit: String? = null

    companion object{

        fun saveOneProduct(list : Product){
            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()
            if(!getAllOfflineItemsNames().contains(list.listName)){
                var item : ItemsListDAO =
                    ItemsListDAO()
                item.itemCategory = list.listName
                item.itemName = list.item!!
                item.itemQuantity = list.quantity
                item.itemUnit = list.unit

                itemsListBox.put(item)
            }
        }

        fun saveCategoryWithNullProduct(category : String){
            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()
            if(!getAllOfflineItemsCategories().contains(category)){
                var item : ItemsListDAO =
                    ItemsListDAO()
                item.itemCategory = category
                itemsListBox.put(item)
            }
        }

        fun saveAllOfflineData(context : Context) {

            val gson = Gson()

            val jsonfile: String = context.assets.open("offlineItemsList.json").bufferedReader().use {it.readText()}

            val allProductsModel: AllProducts = gson.fromJson(jsonfile,
                AllProducts::class.java
            )

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()

           if ((itemsListBox != null && itemsListBox.isEmpty) || (itemsListBox.count() == 0L)) {

                for (groceryList in allProductsModel.myGrocerylist!!) {

                    for (product in groceryList.products!!) {

                        var item : ItemsListDAO =
                            ItemsListDAO()
                        item.itemCategory = groceryList.listName!!
                        item.itemName = product.item!!
                        item.itemQuantity = product.quantity!!
                        item.itemUnit = product.unit!!

                        itemsListBox.put(item)
                    }

                }
           }
        }

        fun getAllOfflineData() : ArrayList<Product> {

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<ItemsListDAO> = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .build().find()

            var productsList : ArrayList<Product> = ArrayList()

            for(product in result){
                productsList.add(
                    Product(
                        product.itemName,
                        product.itemQuantity,
                        product.itemUnit,
                        product.itemCategory
                    )
                )
            }

            return productsList
        }

        fun getAllOfflineItemsNames() : ArrayList<String> {

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<ItemsListDAO> = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .build().find()

            var productsList : ArrayList<String> = ArrayList()

            for(product in result){
                productsList.add(product.itemName!!)
            }

            return productsList
        }

        fun searchItemNamesByCategory(category : String?) : ArrayList<Product> {

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()

            val result: List<ItemsListDAO> = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .equal(ItemsListDAO_.itemCategory, category)
                .build().find()

            var productsList : ArrayList<Product> = ArrayList()

            for(product in result){
                productsList.add(
                    Product(
                        product.itemName,
                        product.itemQuantity,
                        product.itemUnit,
                        product.itemCategory
                    )
                )
            }

            return productsList
        }

        fun getAllOfflineItemsCategories() : ArrayList<String> {

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<ItemsListDAO> = itemsListBox.query()
                .notNull(ItemsListDAO_.itemCategory)
                .build().find()

            var productsList : ArrayList<String> = ArrayList()

            for(product in result){
                if(!productsList.contains(product.itemCategory!!)){
                    productsList.add(product.itemCategory!!)
                }
            }

            return productsList
        }

        fun searchItemByName(searchKey : String?) : ArrayList<Product> {

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()

            val result: List<ItemsListDAO> = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .equal(ItemsListDAO_.itemName, searchKey)
                .build().find()

            var productsList : ArrayList<Product> = ArrayList()

            for(product in result){
                productsList.add(
                    Product(
                        product.itemName,
                        product.itemQuantity,
                        product.itemUnit,
                        product.itemCategory
                    )
                )
            }

            return productsList
        }

        fun updateItemByName(searchKey : String?, quantity : String){

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()

            val result: ItemsListDAO = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .equal(ItemsListDAO_.itemName, searchKey)
                .build().findUnique()!!

            result.itemQuantity = quantity
            itemsListBox.put(result)
        }

        fun deleteItemByName(searchKey : String?){

            val itemsListBox: Box<ItemsListDAO> = ObjectBoxUtil.boxStore.boxFor()

            val result: ItemsListDAO = itemsListBox.query()
                .notNull(ItemsListDAO_.itemName)
                .equal(ItemsListDAO_.itemName, searchKey)
                .build().findUnique()!!

            itemsListBox.remove(result)
        }

    }
}
