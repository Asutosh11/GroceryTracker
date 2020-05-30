package `in`.thoughtleaf.grocerytracker.util


class AppConstantsUtil {

    companion object{

        sealed class AdditionScreenType {
            class AddProduct() : AdditionScreenType()
            class AddCategory() : AdditionScreenType()
        }
    }


}