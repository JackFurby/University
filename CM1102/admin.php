<?php
session_start();
?>

<?php
if ($_POST["productSubmit"] == "Submit") {

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

  $id = $_POST["idIn"];
  $cat = $_POST["catIn"];
  $name = $_POST["nameIn"];
  $desc = $_POST["descIn"];
  $price = $_POST["priceIn"];
  $img = $_POST["imgIn"];

  $id = mysqli_real_escape_string($id);
  $cat = mysqli_real_escape_string($cat);
  $name = mysqli_real_escape_string($name);
  $desc = mysqli_real_escape_string($desc);
  $price = mysqli_real_escape_string($price);
  $img = mysqli_real_escape_string($img);

  //adds new product to the database
  if ($_POST["EditOpt"] == "newProduct") {

    $sql = "INSERT INTO cm1102_products (category, name, description, price, image)
    VALUES ('$cat', '$name', '$desc', '$price', '$img')";

    if ($con->query($sql) === TRUE) {
      echo "<div id='adminArea'>";
      echo "Product added\n";
      echo '<script type="text/javascript">';
      echo 'setTimeout("location.href = \'admin.php\'",5000);';
      echo '</script>';
    } else {
      echo "Error: " . $sql . "<br>" . $con->error;
    }

    //edit a product in the database
  } else if ($_POST["EditOpt"] == "editProduct") {
    if ($_POST["Editdone"] == "true") {
      $sql = "UPDATE cm1102_products SET category='$cat', name='$name', description='$desc', price='$price', image='$img' WHERE ID='$id'";

      if ($con->query($sql) === TRUE) {
        echo "Product updated\n";
        echo '<script type="text/javascript">';
        echo 'setTimeout("location.href = \'admin.php\'",5000);';
        echo '</script>';
      } else {
        echo "Error updating record: " . $con->error;
      }

//W3schools.com. (n.d.). PHP Update Data in MySQL. [online] Available at: https://www.w3schools.com/php/php_mysql_update.asp [Accessed 2 Apr. 2017].

        mysql_close($con);
    } else {

      $command = "SELECT ID, name, description, price, image, category FROM cm1102_products WHERE ID = $id";

		  // Execute the query for products
		  $result = mysqli_query($con, $command);
      while($row = mysqli_fetch_assoc($result)) {
    echo"
    <!DOCTYPE html>
    <meta charset=\"utf-8\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>
    <html>
    	<head id=\"header\">
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"css/admin.css\">
    		<title>Retro Tech - Admin</title>
    	</head>
    	<body>
        <div id='adminArea'>
          <h1>Retro Tech Admin</h1>
          <form name=\"shopEditForm\" action=\"admin.php\" method=\"POST\">

            <input name=\"Editdone\" value=\"true\" hidden>
            <input name=\"EditOpt\" value=\"editProduct\" hidden>

            <h3 id=\"editIdH3\">ID</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"idIn\" placeholder=\"Product ID\" id=\"editIdIn\" value=" . $row["ID"]. ">
            </div>
            <h3 id=\"editCatH3\">Category</h3>
            <div class=\"paymentForm\" id=\"cardNumberArea\">
              <input class='textIn' name=\"catIn\" placeholder=\"Product category\" id=\"editCatIn\" value=" . $row["category"]. ">
            </div>
            <h3 id=\"editNameH3\">Name</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"nameIn\" placeholder=\"Product name\" id=\"editNameIn\" value=" . $row["name"]. ">
            </div>
            <h3 id=\"editDescH3\">Description</h3>
            <div class=\"AdminProductForm\">
              <textarea class='textIn' name=\"descIn\" placeholder=\"Product description\" id=\"editDescIn\">" . $row["description"]. "</textarea>
            </div>
            <h3 id=\"editPriceH3\">Price</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"priceIn\" placeholder=\"Product price\" id=\"editPriceIn\" value=" . $row["price"]. ">
            </div>
            <h3 id=\"editImgH3\">Image</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"imgIn\" placeholder=\"Product image\" id=\"editImgIn\" value=" . $row["image"]. ">
            </div>
            <h3>Submit</h3>
            <input type=\"submit\" name=\"productSubmit\" value=\"Submit\">
            <input type=\"reset\" name=\"reset\" value=\"Reset\">
          </form>
          <form name=\"logout\" method=\"POST\">
            <input type=\"submit\" name=\"logoutBtn\" value=\"Logout\" />
            <p></p>
          </form>
        </div>
      </body>
    </html>
    ";
  }
    mysqli_close($con);
  }

  //remove product from database
  } else if ($_POST["EditOpt"] == "removeProduct") {
    $sql = "DELETE FROM cm1102_products WHERE id=$id";

    if ($con->query($sql) === TRUE) {
      echo "<div id='adminArea'>";
      echo "Product removed\n";
      echo '<script type="text/javascript">';
      echo 'setTimeout("location.href = \'admin.php\'",5000);';
      echo '</script>';
    } else {
      echo "Error deleting product: " . $con->error;
    }
  }
}
mysqli_close($con);
?>

<?php

  if ($_POST["logoutBtn"] == "Logout") {
    session_destroy();
    header("Refresh:0");
  }

  //form for adding, removing and editing products
  if (isset($_SESSION["username"]) && ($_POST["EditOpt"] != "editProduct")) {

    echo "
    <!DOCTYPE html>
    <meta charset=\"utf-8\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>
    <html>
    	<head id=\"header\">
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"css/admin.css\">
    		<title>Retro Tech - Admin</title>
    	</head>
    	<body>
        <div id='adminArea'>
          <h1>Retro Tech Admin</h1>
          <form name=\"shopEditForm\" action=\"admin.php\" method=\"POST\">
            <div id='adminOptionBtns'>
              <input type='radio' name='EditOpt' value='editProduct' id='editRadBtn' onclick='showAreaEdit()'><label for='editRadBtn'>Edit product</label><br>
              <input type='radio' name='EditOpt' value='newProduct' id='newRadBtn' onclick='showAreaNew()'><label for='newRadBtn'>Add product</label><br>
              <input type='radio' name='EditOpt' value='removeProduct' id='removeRadBtn' onclick='showAreaRemove()'><label for='removeRadBtn'>Remove product</label><br>
            </div>

            <h3 id=\"itemIdH3\">ID</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"idIn\" placeholder=\"Product ID\" id=\"itemIdIn\">
            </div>
            <h3 id=\"itemCatH3\">Category</h3>
            <div class=\"paymentForm\" id=\"cardNumberArea\">
              <input class='textIn' name=\"catIn\" placeholder=\"Product category\" id=\"itemCatIn\">
            </div>
            <h3 id=\"itemNameH3\">Name</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"nameIn\" placeholder=\"Product name\" id=\"itemNameIn\">
            </div>
            <h3 id=\"itemDescH3\">Description</h3>
            <div class=\"AdminProductForm\">
              <textarea class='textIn' name=\"descIn\" placeholder=\"Product description\" id=\"itemDescIn\"></textarea>
            </div>
            <h3 id=\"itemPriceH3\">Price</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"priceIn\" placeholder=\"Product price\" id=\"itemPriceIn\">
            </div>
            <h3 id=\"itemImgH3\">Image</h3>
            <div class=\"AdminProductForm\">
              <input class='textIn' name=\"imgIn\" placeholder=\"Product image\" id=\"itemImgIn\">
            </div>
            <h3>Submit</h3>
            <input type=\"submit\" name=\"productSubmit\" value=\"Submit\">
            <input type=\"reset\" name=\"reset\" value=\"Reset\">
          </form>
          <form name=\"logout\" method=\"POST\">
            <input type=\"submit\" name=\"logoutBtn\" value=\"Logout\" />
            <p></p>
          </form>
        </div>
      </body>
    </html>
    ";
  } else if ($_POST["EditOpt"] != "editProduct") {
    $username = (isset($_POST['username']) ? $_POST['username'] : null);
    $password = (isset($_POST['password']) ? $_POST['password'] : null);
  if (("Admin" == $username) && ("123" == $password)) {
    $_SESSION['username'] = $username;
    header("Refresh:0");
  } else {
    if (null != $username) {
      echo "Wrong username or password!";
    }
    echo "
    <!DOCTYPE html>
    <meta charset=\"utf-8\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>
    <html>
    	<head id=\"header\">
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"css/admin.css\">
    		<title>Retro Tech - Admin</title>
    	</head>
    	<body>
        <div id='adminArea'>
        ";
    echo "<form action='admin.php' method=\"POST\">\n";
    echo "Username: <input name=\"username\" type=\"text\"><br />\n";
    echo "Password: <input name=\"password\" type=\"password\">\n";
    echo "<input type=\"submit\" value=\"Submit\">\n";
    echo "</form>\n";
    echo "</div>\n";
    }
  }
?>

<noscript>You need javascript enabled</noscript>

<!--page javascipt used for displaying items in form-->
<script>

var idIn = document.getElementById("itemIdIn");
var catIn = document.getElementById("itemCatIn");
var nameIn = document.getElementById("itemNameIn");
var DescIn = document.getElementById("itemDescIn");
var priceIn = document.getElementById("itemPriceIn");
var imgIn = document.getElementById("itemImgIn");

function showAreaEdit() {
  idIn.style.display = 'inline-block';
  idIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  catIn.style.display = 'none';
  catIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  nameIn.style.display = 'none';
  nameIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  DescIn.style.display = 'none';
  DescIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  priceIn.style.display = 'none';
  priceIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  imgIn.style.display = 'none';
  imgIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
}
function showAreaNew() {
  idIn.style.display = 'none';
  idIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  catIn.style.display = 'inline-block';
  catIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  nameIn.style.display = 'inline-block';
  nameIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  DescIn.style.display = 'inline-block';
  DescIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  priceIn.style.display = 'inline-block';
  priceIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  imgIn.style.display = 'inline-block';
  imgIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
}
function showAreaRemove() {
  idIn.style.display = 'inline-block';
  idIn.parentElement.previousSibling.previousElementSibling.style.display = 'inline-block';
  catIn.style.display = 'none';
  catIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  nameIn.style.display = 'none';
  nameIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  DescIn.style.display = 'none';
  DescIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  priceIn.style.display = 'none';
  priceIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
  imgIn.style.display = 'none';
  imgIn.parentElement.previousSibling.previousElementSibling.style.display = 'none';
}
</script>
