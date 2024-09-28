package com.together_english.deiz.security.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.UUID

@Service
class S3ImageUploadService(
        private val amazonS3Client: AmazonS3
) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    @Throws(IOException::class)
    fun uploadFile(file: MultipartFile): String {
        val fileName = generateFileName(file.originalFilename!!)
        val metadata = ObjectMetadata()
        metadata.contentLength = file.size

        amazonS3Client.putObject(bucket, fileName, file.inputStream, metadata)
        return amazonS3Client.getUrl(bucket, fileName).toString() // 업로드된 파일의 URL 반환
    }

    private fun generateFileName(originalFilename: String): String {
        val fileExtension = originalFilename.substringAfterLast(".")
        return "${UUID.randomUUID()}.$fileExtension"
    }
}
