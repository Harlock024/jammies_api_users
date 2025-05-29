package dev.jammies.jammies_api_users.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;


import io.github.cdimascio.dotenv.Dotenv;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
   public CloudinaryService(
    ) {

        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", System.getenv("CLOUDINARY_NAME"),
            "api_key", System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET")
        ));
    }
    public Map upload(@NotNull MultipartFile file, String folder, String resourceType) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", resourceType,
            "folder", folder
        ));
    }
}
