<?php
// File: api/routes/api.php
require_once __DIR__ . '/../controllers/AuthController.php';

// Simple router for demo (use a framework in production)
$uri = $_SERVER['REQUEST_URI'];
$method = $_SERVER['REQUEST_METHOD'];

// parse path
$path = parse_url($uri, PHP_URL_PATH);
$path = str_replace('/api/', '', ltrim($path, '/'));

$body = json_decode(file_get_contents('php://input'), true);
$auth = new AuthController();

if ($path === 'auth/register' && $method === 'POST') {
    echo $auth->register($_POST ?: $body);
} elseif ($path === 'auth/login' && $method === 'POST') {
    echo $auth->login($_POST ?: $body);
} elseif ($path === 'auth/verify' && $method === 'POST') {
    echo $auth->verifyEmail($_POST ?: $body);
} elseif ($path === 'auth/forgot-password' && $method === 'POST') {
    echo $auth->forgotPassword($_POST ?: $body);
} else {
    http_response_code(404);
    echo json_encode(["success" => false, "message" => "Ruta no encontrada"]);
}
?>
