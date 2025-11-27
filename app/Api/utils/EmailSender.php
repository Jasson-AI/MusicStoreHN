<?php
// File: api/utils/EmailSender.php
class EmailSender {
    public static function sendVerificationCode($toEmail, $name, $code) {
        // Implementación básica. Recomiendo usar PHPMailer o similar en producción.
        $subject = "Código de verificación";
        $message = "Hola $name,\n\nTu código de verificación es: $code\n\nSaludos,\nMusicStoreHN";
        $headers = "From: no-reply@musicstorehn.com\r\n";
        // mail($toEmail, $subject, $message, $headers);
        // Para debugging:
        error_log("SendVerificationCode to $toEmail: $code");
    }

    public static function sendTempPassword($toEmail, $name, $tempPassword) {
        $subject = "Contraseña temporal";
        $message = "Hola $name,\n\nTu contraseña temporal es: $tempPassword\nCámbiala al iniciar sesión.\n\nSaludos,\nMusicStoreHN";
        $headers = "From: no-reply@musicstorehn.com\r\n";
        // mail($toEmail, $subject, $message, $headers);
        error_log("SendTempPassword to $toEmail: $tempPassword");
    }
}
?>
