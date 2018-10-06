<?php
session_start();

$item_id = $_GET['value'];

if (isset($_SESSION['user_id'])) {
  $remove_array = preg_grep("/^$item_id$/", $_SESSION[$_cart_items]);
	$_SESSION[$_cart_items] = array_diff($_SESSION[$_cart_items], $remove_array);
} else {
	$_SESSION['user_id'] = uniqid();
}
?>
