<?php
session_start();
if (!isset($_SESSION['user_id'])) {
	$_SESSION['user_id'] = uniqid();
}
?>

<noscript>You need javascript enabled</noscript>
<!--page javascipt start-->
<script>
function showMsg(msg) { //show help message on focus
		msg.style.display = 'inline-block';
}
function hideMsg(msg) {
	msg.style.display = 'none';
}
//removes one item from the cart with a given id
function removeOneItemFromCart(item) {

		if (window.XMLHttpRequest) {
	    xhttp = new XMLHttpRequest();
	  } else {
	    // code for IE6, IE5
	    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    }
		};
		xhttp.open("GET", "removeOneFromCart.php?value=" + item, true);
		xhttp.send();
		document.location.reload(true);
}
//adds one item to the cart with a given id
function addOneItemToCart(item) {

	if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
  } else {
    // code for IE6, IE5
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    }
	};
	xhttp.open("GET", "addToCart.php?value=" + item, true);
	xhttp.send();
	document.location.reload(true);
}
//remove all items from the cart with a given id
function removeAllItemFromCart(item) {

	if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
  } else {
    // code for IE6, IE5
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    }
	};
	xhttp.open("GET", "removeAllItemFromCart.php?value=" + item, true);
	xhttp.send();
	document.location.reload(true);
}
</script>
<!--page javascipt start-->

<!DOCTYPE html>
<meta charset="utf-8" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<html>
	<head id="header">
		<link rel="stylesheet" type="text/css" href="css/cart.css">
		<meta charset="utf-8">
		<title>Retro Tech - Basket</title>
	</head>
	<body>
			<!--Site nav bar start-->
      <nav>
        <ul id="taskBar">
          <li id="menuBtn"><a href="home.php" >Retro Tech</a></li>
          <li id="cart"><a href="cart.php">Cart</a></li>
        </ul>
      </nav>
			<!--Site nav bar end-->
			<!--Site background start-->
      <div id="bField"></div>
			<!--Site background end-->
			<!--Site title window start-->
			<div id='title' onclick="window.location='home.php';">
				<div class="winControl">
					<ul>
	          <li id="XDead"><a>X</a></li>
	        </ul>
				</div>
				<h1>Retro Tech</h1>
			</div>
			<!--Site title window end-->
			<!--Site cart window start-->
			<div id='cartSideBtn' onclick="window.location='cart.php';">
				<h1>Cart</h1>
			</div>
			<!--Site cart window end-->
			<!--Site menu window start-->
			<div id='siteWindow'>
				<div class="winControl">
					<ul>
	          <li id="XDead"><a>X</a></li>
						<li class='windowTitle' style='float:left'><h3>Categories</h3></li>
	        </ul>
				</div>
				<div id="categoriesNav">
					<ul id="productCatNav">
						<?php
						//loggin details for mysql
						$servername = "csmysql.cs.cf.ac.uk";
						$username = "c1619450";
						$password = "nibnugbat6";
						$dbname = "c1619450";
						//connect to mysql
						$con = mysqli_connect($servername, $username, $password, $dbname);
						//error checking for connecting to mysql
						if (!$con) {
							die("Failed to connect: " . mysqli_connect_error());
						}
						// command to get categorys from DB
						$commandCat = "SELECT DISTINCT category FROM cm1102_products";
						// Execute the query for categorys
						$resultCat = mysqli_query($con, $commandCat);
						while($rowCat = mysqli_fetch_assoc($resultCat)) {
							echo"<li><a href='home.php#".$rowCat['category']."'>".$rowCat['category']."</a></li>";
						}
						?>
					</ul>
				</div>
			</div>
			<!--Site menu window end-->
			<!--basket section start-->
			<div id='siteWindow'>
				<div class='winControl'>
					<ul>
						<li id='XDead'><a>X</a></li>
						<li class='windowTitle' style='float:left'><h3>Cart</h3></li>
					</ul>
				</div>
        <!--Basket content start-->
				<?php
				echo "<div id='cartCont'>";
				if (isset($_SESSION[$_cart_items])) {

					$cart_total = 0.00; //total for cart

					$array_counted = array_count_values($_SESSION[$_cart_items]);
					ksort($array_counted); //sorts array of items by key (item id)

					foreach ($array_counted as $item_in_cart => $quantity) {

						//loggin details for mysql
						$servername = "csmysql.cs.cf.ac.uk";
						$username = "c1619450";
						$password = "nibnugbat6";
						$dbname = "c1619450";
						//connect to mysql
						$con = mysqli_connect($servername, $username, $password, $dbname);
						//error checking for connecting to mysql
						if (!$con) {
							die("Failed to connect: " . mysqli_connect_error());
						}

						//build command (better for sql injection)
						$commandPro = $con->prepare("SELECT ID, name, price, image FROM cm1102_products WHERE ID = ?");
						$commandPro->bind_param('s', $item_in_cart);
						$commandPro->execute();

						// Execute the query for products
						$result = $commandPro->get_result();
				    //displays products
						while ($row = $result->fetch_assoc()) {

							$item_total = (float)$row["price"] * (int)$quantity;
							$item_total_formatted = "£ ".number_format($item_total, 2);
							$cart_total += $item_total;

							echo"
								<!--Product content start-->
								<div id='productCont'>
									<div id='itemImg'>
										<img src='".$row['image']."' alt='" . $row["name"]. "' style='width:150px;'>
									</div>
									<div id='itemInfo'>
										<h4 id='itemName'>" . $row["name"]. "</h4>
										<p id='itemID'>Product code: " . $row["ID"]. "</p>
										<p id='itemPrice'>£" . $row["price"]. "</p>
										<p id='itemQuantity'>Quantity: $quantity</p>
										<p id='itemTotal'>Total: $item_total_formatted</p>
										<div id='itemControl'>
											<div id='itemUp' onclick='addOneItemToCart(" . $row["ID"]. ")' onmouseover='showMsg(this.nextSibling.nextElementSibling)' onmouseout='hideMsg(this.nextSibling.nextElementSibling)'>^</div>
											<span class='cartMsg'>Add one item</span>
											<div id='itemDown' onclick='removeOneItemFromCart(" . $row["ID"]. ")' onmouseover='showMsg(this.nextSibling.nextElementSibling)' onmouseout='hideMsg(this.nextSibling.nextElementSibling)'>^</div>
											<span class='cartMsg'>Remove one item</span>
											<div id='itemRemove' onclick='removeAllItemFromCart(" . $row["ID"]. ")' onmouseover='showMsg(this.nextSibling.nextElementSibling)' onmouseout='hideMsg(this.nextSibling.nextElementSibling)'>X</div>
											<span class='cartMsg'>Remove item from cart</span>
										</div>
									</div>
							</div>
			        <!--Procuct content end-->";
						}
					}
					$cart_total_format = "£ ".number_format($cart_total, 2); //total value of items in cart formatted as £

					echo"
					<div>
						<p>Cart total: $cart_total_format</p>
					</div>";
				}	else {
					echo "<p>No items in cart</p>";
				}
				?>
				</div>
        <a href="pay.php" id="checkout">Checkout</a>
        <!--Basket content end-->
			 </div>
			 <!--basket section end-->
	</body>
</html>
