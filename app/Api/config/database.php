<?php
// File: api/config/database.php

class Database {

    // DATOS DE TU INSTANCIA CLOUD SQL
    private $host = "34.136.90.148";
    private $port = "3306";
    private $db_name = "music_store_hn";
    private $username = "root";
    private $password = "";   // <-- si tu instancia tiene clave, PÓNLA AQUÍ

    public $conn;

    public function getConnection() {

        $this->conn = null;

        try {
            $dsn = "mysql:host={$this->host};port={$this->port};dbname={$this->db_name};charset=utf8mb4";

            $this->conn = new PDO(
                $dsn,
                $this->username,
                $this->password,
                [
                    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
                ]
            );

        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode([
                "success" => false,
                "message" => "Error de conexión a la base de datos",
                "error" => $e->getMessage()
            ]);
            exit;
        }

        return $this->conn;
    }
}
?>
