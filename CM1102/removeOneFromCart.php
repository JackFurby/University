<?php
session_start();

$item_id = $_GET['value'];

if (isset($_SESSION['user_id'])) {
	if(($key = array_search($item_id, $_SESSION[$_cart_items])) !== false) {
	    unset($_SESSION[$_cart_items][$key]);
//stackoverflow.com. (2014). PHP array delete by value (not key). [online] Available at: http://stackoverflow.com/questions/7225070/php-array-delete-by-value-not-key [Accessed 2 Apr. 2017].
	}
} else {
	$_SESSION['user_id'] = uniqid();
}
?>
