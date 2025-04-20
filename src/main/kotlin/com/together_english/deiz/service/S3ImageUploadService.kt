package com.together_english.deiz.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.URL
import java.util.UUID

@Service
class S3ImageUploadService(
    private val amazonS3Client: AmazonS3
) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    /**
     * S3에 파일을 업로드하고 업로드된 파일의 URL을 반환
     * @param file 업로드할 MultipartFile
     * @return 업로드된 파일의 S3 URL
     * @throws IOException 파일 입출력 오류 발생 시
     */
    @Throws(IOException::class)
    fun uploadFile(file: MultipartFile): String {
        val fileName = generateFileName(file.originalFilename ?: throw IllegalArgumentException("파일 이름이 없습니다."))
        val metadata = ObjectMetadata()
        metadata.contentLength = file.size

        amazonS3Client.putObject(bucket, fileName, file.inputStream, metadata)
        return amazonS3Client.getUrl(bucket, fileName).toString()
    }

    /**
     * S3에서 지정된 파일을 삭제
     * @param fileKey S3 버킷 내 삭제할 파일의 키 (예: uuid.jpg)
     * @throws IllegalArgumentException 파일 키가 유효하지 않을 경우
     */
    fun deleteFile(fileKey: String) {
        if (fileKey.isBlank()) {
            throw IllegalArgumentException("파일 키가 비어있습니다.")
        }
        amazonS3Client.deleteObject(bucket, fileKey)
    }

    /**
     * S3 URL에서 파일 키를 추출하여 해당 파일을 삭제
     * @param fileUrl S3 파일의 URL (예: https://english-together.s3.ap-northeast-2.amazonaws.com/60314d51-c659-4ac1-9e01-57a9804fface.jpeg)
     * @throws IllegalArgumentException URL이 유효하지 않거나 파일 키를 추출할 수 없을 경우
     */
    fun deleteFileFromUrl(fileUrl: String) {
        if (fileUrl.isBlank()) {
            throw IllegalArgumentException("파일 URL이 비어있습니다.")
        }
        try {
            val url = URL(fileUrl)
            val fileKey = url.path.removePrefix("/") // 경로에서 선행 슬래시 제거
            if (fileKey.isBlank()) {
                throw IllegalArgumentException("파일 키를 URL에서 추출할 수 없습니다.")
            }
            deleteFile(fileKey)
        } catch (e: Exception) {
            throw IllegalArgumentException("유효하지 않은 URL입니다: $fileUrl", e)
        }
    }

    /**
     * 고유한 파일 이름을 생성
     * @param originalFilename 원본 파일 이름
     * @return UUID 기반의 새 파일 이름
     */
    private fun generateFileName(originalFilename: String): String {
        val fileExtension = originalFilename.substringAfterLast(".", "")
        if (fileExtension.isBlank()) {
            throw IllegalArgumentException("파일 확장자가 없습니다.")
        }
        return "${UUID.randomUUID()}.$fileExtension"
    }
}