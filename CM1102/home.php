<?php
session_start();
if (!isset($_SESSION['user_id'])) {
	$_SESSION['user_id'] = uniqid();
}
?>

<noscript>You need javascript enabled</noscript>
<!--Site javascipt start-->
<script>
function hideArea(item) {
	item.style.display = 'none';
}
function showArea(item) {
	item.style.display = 'inline-block';
}
function addToCart(item, msg) {

	if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
  } else {
    // code for IE6, IE5
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.onreadystatechange = function() {
		//if product is added to card display msg
    if (this.readyState == 4 && this.status == 200) {
			msg.style.display = 'inline-block';
			setTimeout(function(){
      msg.style.display = "none";
      },3000);
    }
	};
	xhttp.open("GET", "addToCart.php?value=" + item, true);
	xhttp.send();
}
</script>
<!--Site javascipt end-->

<!DOCTYPE html>
<meta charset="utf-8" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<html>
	<head id="header">
		<link rel="stylesheet" type="text/css" href="css/home.css">
		<meta charset="utf-8">
		<title>Retro Tech</title>
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
		<!--Site product menu window start-->
		<div id='siteWindow'>
			<div class="winControl">
				<ul>
	         <li id="XDead"><a>X</a></li>
					<li class='catTitle' style='float:left'><h3>Categories</h3></li>
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
						echo"<li onclick='showArea(".$rowCat['category'].".parentElement.parentElement.parentElement)'><a href=#".$rowCat['category'].">".$rowCat['category']."</a></li>";
					}
					?>
				</ul>
			</div>
			<!--product sort start-->
			<div id="productSort">
			<h3>Order products</h3>
			<form id="productSortForm" method="post">
				<input type='radio' name='orderProducts' value='orderNameAZ' id='nameAZ'><label for='nameAZ'>Name A-Z</label><br>
				<input type='radio' name='orderProducts' value='orderNameZA' id='nameZA'><label for='nameZA'>Name Z-A</label><br>
				<input type='radio' name='orderProducts' value='orderPriceLowHigh' id='priceLow'><label for='priceLow'>Price (Low to High)</label><br>
				<input type='radio' name='orderProducts' value='orderPriceHighLow' id='priceHigh'><label for='priceHigh'>Price (High to Low)</label><br>
				<input type="submit" name="productSortSubmit" value="Submit">
			</form>
			</div>
			<!--product sort end-->
		</div>
		<!--Site product menu window end-->
		<!--Site php start-->
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
		//sets order of product in categorys
		if (isset($_POST["orderProducts"])) {
			if ($_POST["orderProducts"] == orderNameAZ) {
				// command to get products
				$command = "SELECT ID, name, description, price, image FROM cm1102_products WHERE category = '".$rowCat['category']."' ORDER BY name ASC";
			} elseif ($_POST["orderProducts"] == orderNameZA){
				$command = "SELECT ID, name, description, price, image FROM cm1102_products WHERE category = '".$rowCat['category']."' ORDER BY name DESC";
			} elseif ($_POST["orderProducts"] == orderPriceLowHigh){
				$command = "SELECT ID, name, description, price, image FROM cm1102_products WHERE category = '".$rowCat['category']."' ORDER BY price ASC";
			} elseif ($_POST["orderProducts"] == orderPriceHighLow){
				$command = "SELECT ID, name, description, price, image FROM cm1102_products WHERE category = '".$rowCat['category']."' ORDER BY price DESC";
			}
		} else {
			$command = "SELECT ID, name, description, price, image FROM cm1102_products WHERE category = '".$rowCat['category']."' ORDER BY price ASC";
			}
		// Execute the query for products
		$result = mysqli_query($con, $command);
    //displays products
		echo"
		<div id='products' class=".$rowCat['category'].">
			<div class='winControl'>
				<ul>
					<li class='X' onclick='hideArea(this.parentElement.parentElement.parentElement)'><a href='#home.html'>X</a></li>
					<li class='catTitle' style='float:left' id=".$rowCat['category']."><h3>".$rowCat['category']."</h3></li>
				</ul>
			</div>";
    while($row = mysqli_fetch_assoc($result)) {
			echo"
			<div id='divItem' value=" . $row["ID"]. ">
				<div id='itemView'>
					<img id='itemImg' src='".$row['image']."' alt='" . $row["name"]. "' style='width:200px;'>
					<a href='product.php?value=" . $row["ID"]. "' id='itemDes'>". $row["description"]."</a>
					<a href='product.php?value=" . $row["ID"]. "' id='itemViewMore'>View more</a>
				</div>
				<div id='itemInfoShort'>
					<h4 id='itemName'>" . $row["name"]. "</h4>
					<p id='itemPrice'>£" . $row["price"]. "</p>
					<p id='itemID'>Product code: " . $row["ID"]. "</p>
					<a value=" . $row["ID"]. "' id='productAddToCart' onclick='addToCart(" . $row["ID"]. ",this.nextSibling.nextElementSibling)'>Add to cart</a>
					<span class='cartMsg'>Product added to cart</span>
				</div>
			</div>";
		 }
			echo"
		</div>";
		}
		// Close the connection
		mysqli_close($connect);
		?>
		<!--Site php end-->
	</body>
</html>
<!--
Product referiences:

Nintendo 64
Image:
Evan-Amos, (2014). Nintendo-64-wController-L.jpg. [image] Available at: https://en.wikipedia.org/wiki/Nintendo_64#/media/File:Nintendo-64-wController-L.jpg [Accessed 26 Feb. 2017]
Description:
(http://www.basemedia.com.au/), B. (2017). Nintendo 64. [online] Console Database. Available at: http://www.consoledatabase.com/consoleinfo/nintendo64/ [Accessed 27 Feb. 2017]

Atari-2600
Image:
Evan-Amos, (2011). An Atari 2600 four-switch "wood veneer" version, dating from 1980-1982. Shown with standard joystick.. [image] Available at: https://en.wikipedia.org/wiki/Atari_2600#/media/File:Atari-2600-Wood-4Sw-Set.jpg [Accessed 27 Feb. 2017].
Description:
Amazon.co.uk. (n.d.). Atari 2600 Console: Amazon.co.uk: PC & Video Games. [online] Available at: https://www.amazon.co.uk/Atari-2600-Console/dp/B002QKD2S4 [Accessed 27 Feb. 2017].

Intel Pentium Pro
Image:
Mixeurpc, (2017). Intel Pentium Pro CPU images. [image] Available at: http://mixeurpc.free.fr/SITE_x86-guide/Photos/Grandes/138/P%20PRO%20200%20256%20SL245.jpg [Accessed 27 Feb. 2017].
Description:
Pentium Pro. (n.d.). In: Wikipedia, 1st ed

Intel 4004
image:
computer history museum, (2012). intel_4004_2. [image] Available at: https://computerhistorymuseum.files.wordpress.com/2012/09/intel_4004_2.jpg [Accessed 27 Feb. 2017].
Description:
Intel 4004. (n.d.). In: Wikipedia, 1st ed.

AM486 DX4-100
Image:
Image of AMD486. (n.d.). [image] Available at: http://mixeurpc.free.fr/SITE_x86-guide/Photos/Grandes/25/AMD%20Am486%20DX4-100SV8B%2001.jpg [Accessed 27 Feb. 2017].
Description:
Am486. (n.d.). In: Wikipedia, 1st ed
-->
