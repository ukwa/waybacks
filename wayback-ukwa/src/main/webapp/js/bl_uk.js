/**
 * Currently not in use.
 * 
 * Supports draggable/collapsable toolbar.
 * 
 */
var slideSpeed = 5;
var timer = 10;

var activeSlide = false;
var isSliding = false;
var dragElement = null;
var upArrow = "";
var downArrow = "";

function slideBanner( e )
{
	if( isSliding ) return;
	isSliding = true;
	var timeline = document.getElementById( "timeline_banner" );
	var image = document.getElementById( "timeline_vertical" );

	if( !timeline.style.display || timeline.style.display == "none" ){
		if( activeSlide ){
			doSlide( slideSpeed * -1 );
			image.src = downArrow;
			image.title = "Click to show Timeline";
			document.cookie = "hidden=true;path=/";
		} else {
			image.src = upArrow;
			image.title = "Click to hide Timeline";
			timeline.style.display = "block";
			timeline.style.visibility = "visible";
			document.cookie = "hidden=fals;path=/";
			doSlide( slideSpeed );
		}
	} else {
		doSlide( slideSpeed * -1 );
		image.src = downArrow;
		image.title = "Click to show Timeline";
		activeSlide = false;
		document.cookie = "hidden=true;path=/";
	}
}


function doSlide( direction )
{
	var timeline = document.getElementById( "timeline_banner" );
	var contentObj = document.getElementById( "wm-ipp" );
	height = timeline.clientHeight;
	if( height == 0 ) height = timeline.offsetHeight;
	height = height + direction;
	recurse = true;
	if( height > contentObj.offsetHeight ) {
		height = contentObj.offsetHeight;
		recurse = false;
	}
	if( height <= 1 ){
		height = 1;
		recurse = false;
	}
	timeline.style.height = height + "px";
	var topPos = height - contentObj.offsetHeight;
	if( topPos > 0 )topPos = 0;
	contentObj.style.top = topPos + "px";
	if( recurse ){
		window.setTimeout( "doSlide( " + direction + " )", timer );
	} else {
		if( height <= 1 ){
			timeline.style.display = "none"; 
			if( activeSlide ){
				document.getElementById( "timeline_banner" ).style.display = "block";
				doSlide( slideSpeed );
			} else {
				isSliding = false;
			}
		} else {
			activeSlide = true;
			isSliding = false;
		}
	}
}

function initSlide( up, down )
{
	document.getElementById( "timeline_vertical" ).onclick = slideBanner;
	document.getElementById( "wm-ipp" ).style.top = 0 - document.getElementById( "timeline_banner" ).offsetHeight + "px";
	upArrow = up;
	downArrow = down;
}

function dragAttach( element ) {
	element.onmousedown = dragStart;
 
	element.dragStart = new Function();
	element.drag = new Function();
	element.dragStop = new Function();
 
	return element;
}
function dragStart( e ) {
	var element = dragElement = this;
	var x = "0px";
	element.style.cursor = "move";

	if( element.currentStyle ) {
		x = element.currentStyle.left;
	} else {
		x = window.getComputedStyle( element, null ).getPropertyValue( "left" );
	}
	if( x.indexOf( "%" ) != -1 ) {
		x = document.body.clientWidth * ( parseInt( x.substring( 0, x.length - 1 ) ) / 100 );
	}
	element.style.left = parseInt( x ) + "px";

	e = e ? e : window.event;
	element.mouseX = e.clientX;
 	element.dragStart( element, x );
 
	document.onmousemove = drag;
	document.onmouseup = dragStop;
	return false;
}
function drag( e ) {
	var element = dragElement;
	var x = parseInt( element.style.left );
 
	e = e ? e : window.event;
	element.style.left = x + ( e.clientX - element.mouseX ) + "px";
	element.mouseX = e.clientX;
	element.drag( element, x );
 
	return false;
}
function dragStop() {
	var element = dragElement;
	element.style.cursor = "pointer";
	var x = parseInt( element.style.left );
	element.dragStop( element, x );
	document.onmousemove = null;
	document.onmouseup = null;
	dragElement = null;
}
