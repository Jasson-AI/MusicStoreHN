<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../utils/EmailSender.php';

class AuthController {

    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->getConnection();
    }

    private function respond($success, $message, $data = null) {
        echo json_encode([
            "success" => $success,
            "message" => $message,
            "data"    => $data
        ]);
        exit;
    }

    public function register($data) {
        $name = $data['name'] ?? "";
        $email = $data['email'] ?? "";
        $password = $data['password'] ?? "";

        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $this->respond(false, "Email inválido");
        }

        $stmt = $this->conn->prepare("SELECT id FROM users WHERE email = ?");
        $stmt->execute([$email]);

        if ($stmt->rowCount() > 0) {
            $this->respond(false, "El correo ya está registrado");
        }

        $hashed = password_hash($password, PASSWORD_BCRYPT);
        $code = rand(100000, 999999);

        $stmt = $this->conn->prepare(
            "INSERT INTO users (name, email, password, verification_code)
             VALUES (?, ?, ?, ?)"
        );

        if ($stmt->execute([$name, $email, $hashed, $code])) {
            EmailSender::sendVerificationCode($email, $name, $code);
            $this->respond(true, "Usuario registrado. Verifica tu email.");
        }

        $this->respond(false, "Error al registrar usuario");
    }

    public function login($data) {
        $email = $data['email'] ?? "";
        $password = $data['password'] ?? "";

        $stmt = $this->conn->prepare(
            "SELECT id, name, email, password, profile_image, bio, is_verified
             FROM users WHERE email = ?"
        );
        $stmt->execute([$email]);

        if ($stmt->rowCount() == 0) {
            $this->respond(false, "Correo o contraseña incorrectos");
        }

        $user = $stmt->fetch();

        if (!password_verify($password, $user['password'])) {
            $this->respond(false, "Correo o contraseña incorrectos");
        }

        if (!$user['is_verified']) {
            $this->respond(false, "Debes verificar tu correo");
        }

        // TOKEN SIMPLE
        $token = base64_encode($user['id'] . ":" . time());

        $this->respond(true, "Login exitoso", [
            "id" => $user['id'],
            "name" => $user['name'],
            "email" => $user['email'],
            "profile_image" => $user['profile_image'],
            "bio" => $user['bio'],
            "token" => $token
        ]);
    }

    public function verifyEmail($data) {
        $email = $data['email'] ?? "";
        $code  = $data['code'] ?? "";

        $stmt = $this->conn->prepare(
            "UPDATE users
             SET is_verified = 1, verification_code = NULL
             WHERE email = ? AND verification_code = ?"
        );

        $stmt->execute([$email, $code]);

        if ($stmt->rowCount() > 0) {
            $this->respond(true, "Email verificado");
        }

        $this->respond(false, "Código incorrecto");
    }

    public function forgotPassword($data) {
        $email = $data['email'] ?? "";

        $stmt = $this->conn->prepare(
            "SELECT id, name FROM users WHERE email = ?"
        );
        $stmt->execute([$email]);

        if ($stmt->rowCount() == 0) {
            $this->respond(false, "El correo no existe");
        }

        $user = $stmt->fetch();

        $temp = substr(md5(uniqid()), 0, 8);
        $hashedTemp = password_hash($temp, PASSWORD_BCRYPT);

        $stmt = $this->conn->prepare(
            "INSERT INTO password_resets (email, temp_password, expires_at)
             VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 1 HOUR))"
        );
        $stmt->execute([$email, $hashedTemp]);

        EmailSender::sendTempPassword($email, $user['name'], $temp);

        $this->respond(true, "Contraseña temporal enviada");
    }
}
?>
