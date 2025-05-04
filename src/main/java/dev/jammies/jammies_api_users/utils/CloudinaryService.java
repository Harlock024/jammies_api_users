package dev.jammies.jammies_api_users.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class CloudinaryService {
    private final Cloudinary cloudinary;


    String cloud_name = System.getProperty("CLOUDINARY_NAME");
    String api_key = System.getProperty("CLOUDINARY_API_KEY");
    String api_secret = System.getProperty("CLOUDINARY_API_SECRET");


    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloud_name,
                "api_key", api_key,
                "api_secret", api_secret

        ));
    }

    public Map uploadTrack(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "tracks"

        ));
    }

    public Map UploadTrackCover(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "covers/tracks"
        ));
    }

    public Map uploadPostImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "posts/image"
        ));
    }

    public Map uploadAlbumCover(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "covers/albums"

                ));
    }
    // maybe just this function is better :/
    public Map uploadFileToFolder(MultipartFile file, String folder, String resourceType, String public_id) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", resourceType,
                "folder", folder,
                "public_id", public_id
        ));
    }
}