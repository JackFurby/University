<?php
session_start();

$item_id = $_GET['value'];

if (isset($_SESSION['user_id'])) {
	if (isset($_SESSION[$_cart_items])) {
		array_push($_SESSION[$_cart_items], $item_id);
	}	else {
		$_SESSION[$_cart_items] = array();
		array_push($_SESSION[$_cart_items], $item_id);
	}
} else {
	$_SESSION['user_id'] = uniqid();

//W3schools.com. (n.d.). AJAX Introduction. [online] Available at: https://www.w3schools.com/xml/ajax_intro.asp [Accessed 2 Apr. 2017].

}
?>
