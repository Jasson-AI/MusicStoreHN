<?php
// File: api/utils/FileUploader.php
class FileUploader {
    public static function upload($file, $targetDir = 'uploads/') {
        if (!is_dir($targetDir)) {
            mkdir($targetDir, 0755, true);
        }

        $filename = time() . '_' . basename($file['name']);
        $targetFile = $targetDir . $filename;

        if (move_uploaded_file($file['tmp_name'], $targetFile)) {
            return $targetFile;
        }

        return false;
    }
}
?>
