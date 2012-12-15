<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head>
    <meta http-equiv="Content-Language" content="en" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
	<!-- jGoBoard 2 stylesheet (jgoboard.css), also available in with smaller (jgoboard_small.css) and larger (jgoboard_large.css) board-->
    <link rel="stylesheet" href="../jgo/jgoboard.css" type="text/css" />
    	
	<!-- jGoBoard requires jQuery -->
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
	
	<!-- jGoBoard 2 javascript file -->
    <script type="text/javascript" src="../jgo/jgoboard.js"></script>
    <script type="text/javascript" src="../jgo/sgf.js"></script>
    <title>GO Invitation</title>
</head>
<body>

<div id="gamearea">
<p> <%= request.getAttribute("debug") %> please use the latest Beta of Gobandroid to play in this game you can get it for free on Google-Play: 
<a href="https://play.google.com/store/apps/details?id=org.ligi.gobandroid_hd&referrer=utm_source%3Dinvite%26utm_medium%3Dcloud-goban">
  <img alt="Android app on Google Play"
       src="http://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>
 <br/>Or if you like JavaScript and want to do the Web-Part - <a href="mailto:ligi@ligi.de">drop me a line</a> 
	<div class="jgo_board" id="board"></div>
</div>

<script type="text/javascript">

var board; // this will hold the JGOBoard object that is tied to the DOM table element
var sgf;
var gameTree;
var player;

/* jQuery calls our function when page has finished loading */
$(document).ready(function(){
	board = jgo_generateBoard($("#board")); // generate the board
	board.click = boardClick; // listen to board clicks - JGOCoordinate parameter will be passed
		
	/* Text selection with mouse double click creates artifacts on Chrome at least */
	$("#board").attr('unselectable', 'on').css('-moz-user-select', 'none').each(function() { 
		this.onselectstart = function() { return false; };
	});
	sgf="<%= request.getAttribute("sgf") %>";
	gameTree=jgo_parseSGF(sgf);
    player = new JGOPlayer(gameTree);
    
    while(player.next());
    
	board.clearMarkers();
	
	board.setBoard(player.board);
		
	if("markers" in player.state)
		$.each(player.state["markers"], function(marker, coords) { board.mark(coords, marker); });
	

});

/* jGoBoard calls the click handler when user clicks on a coordinate - parameter is JGOCoordinate object */
function boardClick(coord) {
	var stone = board.get(coord);
	
	if(stone == JGO_CLEAR) // if no stone, put a black one in
		board.set(coord, JGO_BLACK);
	else if(stone == JGO_BLACK) // if a black stone, change it to white
		board.set(coord, JGO_WHITE);
	else // if a white stone, clear the stone
		board.set(coord, JGO_CLEAR);
}

</script>
</body>
</html>
