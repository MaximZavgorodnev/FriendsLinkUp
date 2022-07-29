package ru.maxpek.friendslinkup
//
//private val cropImage = registerForActivityResult(CropImageContract()) { result ->
//    if (result.isSuccessful) {
//        // use the returned uri
//        val uriContent = result.uriContent
//        val uriFilePath = result.getUriFilePath(context) // optional usage
//    } else {
//        // an error occurred
//        val exception = result.error
//    }
//}
//
//private fun startCrop() {
//    // start picker to get image for cropping and then use the image in cropping activity
//    cropImage.launch(
//        options {
//            setGuidelines(Guidelines.ON.)
//        }
//    )
//
//    //start picker to get image for cropping from only gallery and then use the image in
//    //cropping activity
//    cropImage.launch(
//        options {
//            setImagePickerContractOptions(
//                PickImageContractOptions(includeGallery = true, includeCamera = false)
//            )
//        }
//    )
//
//    // start cropping activity for pre-acquired image saved on the device and customize settings
//    cropImage.launch(
//        options(uri = imageUri) {
//            setGuidelines(Guidelines.ON)
//            setOutputCompressFormat(CompressFormat.PNG)
//        }
//    )
//}