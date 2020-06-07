package `in`.thoughtleaf.grocerytracker.data.dao

import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.util.ObjectBoxUtil
import io.objectbox.Box
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor

@Entity
class BuyingListDAO {

    @Id
    var id: Long = 0

    var itemName: String? = null
    var itemCategory: String? = null
    var itemQuantity: String? = null
    var itemUnit: String? = null

    companion object{

        fun addProduct(product: Product) {
            val buyingItemsListBox: Box<BuyingListDAO> = ObjectBoxUtil.boxStore.boxFor()

            var buyingItem : BuyingListDAO = BuyingListDAO()

            buyingItem.itemCategory = product.listName!!
            buyingItem.itemName = product.item!!
            buyingItem.itemQuantity = product.quantity!!
            buyingItem.itemUnit = product.unit!!

            buyingItemsListBox.put(buyingItem)
        }

        fun getAllOfflineData() : ArrayList<Product> {

            val buyingItemsListBox: Box<BuyingListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<BuyingListDAO> = buyingItemsListBox.query()
                .notNull(BuyingListDAO_.itemName)
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

        fun getAllOfflineDataNames() : ArrayList<String> {

            val buyingItemsListBox: Box<BuyingListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<BuyingListDAO> = buyingItemsListBox.query()
                .notNull(BuyingListDAO_.itemName)
                .build().find()

            var productsList : ArrayList<String> = ArrayList()

            for(product in result){
                productsList.add(product.itemName!!)
            }

            return productsList
        }

        fun getAllOfflineDataNamesLowerCase() : ArrayList<String> {

            val buyingItemsListBox: Box<BuyingListDAO> = ObjectBoxUtil.boxStore.boxFor()


            val result: List<BuyingListDAO> = buyingItemsListBox.query()
                .notNull(BuyingListDAO_.itemName)
                .build().find()

            var productsList : ArrayList<String> = ArrayList()

            for(product in result){
                productsList.add(product.itemName!!.toLowerCase())
            }

            return productsList
        }

        fun deleteItemByName(searchKey : String?){

            val buyingItemsListBox: Box<BuyingListDAO> = ObjectBoxUtil.boxStore.boxFor()

            val result: BuyingListDAO = buyingItemsListBox.query()
                .notNull(BuyingListDAO_.itemName)
                .equal(BuyingListDAO_.itemName, searchKey)
                .build().findFirst()!!

            buyingItemsListBox.remove(result)
        }

    }
}