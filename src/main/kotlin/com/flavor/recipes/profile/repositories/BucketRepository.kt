package com.flavor.recipes.profile.repositories

import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Bucket.BlobTargetOption
import com.google.cloud.storage.Storage
import com.google.firebase.cloud.StorageClient
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit


@Repository
class BucketRepository {
    fun saveImage(userID: String, file: ByteArray, contentType: String){
        val bucket: Bucket = StorageClient.getInstance().bucket()
        bucket.create(userID, file, contentType)
    }
    fun getLinkImage(userID: String): String{
        val bucket: Bucket = StorageClient.getInstance().bucket()
        return bucket.get(userID).signUrl(365, TimeUnit.DAYS, Storage.SignUrlOption.withVirtualHostedStyle()).toExternalForm()
    }
}