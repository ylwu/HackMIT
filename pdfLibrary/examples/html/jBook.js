/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info: http://www.idrsolutions.com
* Help section for developers at http://www.idrsolutions.com/java-pdf-library-support/
*
* (C) Copyright 1997-2013, IDRsolutions and Contributors.
*
* This file is part of JPedal
*
@LICENSE@
*
* ---------------
* jBook.js
* ---------------
*/
var BOOKWIDTH = 0;
var BOOKHEIGHT = 0;
var CLIPDIFF = 0;
var BOOKMARGINY = 0;
var BOOKMARGINX = 0;
var screenW = 0;
var isZoomed = false;
var zoomRate = 1;
var currentDrag = 3;
var googletag;
var bookX = 0;
var bookY = 0;
var bgColor = "rgb(105,105,115)";//"purple";//;"#505050";
var boardColor = "rgb(65,65,75)";//"#383838";
var pw = 0;
var ph = 0;
var iter = 15;
var speed = 40;
var jpedalDiv;
var container;
var pages = new Array();
var bookDiv;
var leftDiv;
var rightDiv;
var leftSlides; 
var rightSlides;
var zoomSlide;
var taskDiv;
var isIE = false;
var isSafari = false;
//Drag and clipping element
var dragElement;
var clipElement;
var gradElement;
var dragContent;
var innerClip;
var innerContent;
var dlt;
var drt;
var drb;
var dlb;
var isTouch = false;
var touchStartX = 0;
var touchStartY = 0;
var lastX = 0;
var lastY = 0;

var prev;
var next;

var dltX = 0;
var dltY = 0;
var drtX = 0;
var drtY = 0;
var drbX = 0;
var drbY = 0;
var dlbX = 0;
var dlbY = 0;
var toAnimX = 0;
var toAnimY = 0;
var STX = 0;
var STY = 0;
var SBX = 0;
var SBY = 0;
var INRAD = 0;
var OUTRAD = 0;

var nextX = 6 ;
var pageCount = 0;
var isAjax = false;
var dragBox = -1;
var curRP = 1;
var isTurning = false;
var pageDom = new Array();
var isAnalytics = false;
var isEC = false; //for ec country
var adjustSpacing = true;

var tBarH = 50;
var tweetDiv;
var aPrefix;
function setDragBox(num){
    dragBox = num;
    switch(num){
        case 1:
            updateDragClip("minus");
            break;
        case 2:
            updateDragClip("plus");
            break;
        case 3:
            updateDragClip("plus");
            break;
        case 4:
            updateDragClip("minus");
            break;
    }
}
function makeNonAjaxPage(pageTotal,w,h,prefix){   
    pageCount = pageTotal;
    addCSS(d('userTop'),"position:absolute;z-index:10;");
    addCSS(d('userLeft'),"position:absolute;z-index:10;");
    addCSS(d('userRight'),"position:absolute;z-index:10;");
    addCSS(d('userBottom'),"position:absolute;z-index:10;");
    
    var x = tBarH+getInt(window.getComputedStyle(d('userLeft'),null).getPropertyValue("width"));   
    var y = tBarH+getInt(window.getComputedStyle(d('userTop'),null).getPropertyValue("height")); 
    if(aPrefix!=null && aPrefix.toString().length>0){
        isAnalytics = true;
    } 
    aPrefix = prefix;
    makeBook(x,y,2*w,h);
}
function makeAjaxPage(pageTotal,w,h,prefix){
    isAjax = true;
    pageCount = pageTotal;
    pageDom[0] = "";
    aPrefix = prefix;
    
    addCSS(d('userTop'),"position:absolute;z-index:10;");
    addCSS(d('userLeft'),"position:absolute;z-index:10;");
    addCSS(d('userRight'),"position:absolute;z-index:10;");
    addCSS(d('userBottom'),"position:absolute;z-index:10;");
    
    var x = tBarH+getInt(window.getComputedStyle(d('userLeft'),null).getPropertyValue("width"));   
    var y = tBarH+getInt(window.getComputedStyle(d('userTop'),null).getPropertyValue("height")); 
       
    
    // alert(" "+w+" ... "+h+" "+pageCount);
    if(aPrefix!=null && aPrefix.toString().length>0){
        isAnalytics = true;
    } 
    makeBook(x,y,2*w,h);
}
function makeBook(x,y,w,h){    
            
    if ( navigator.appName == 'Microsoft Internet Explorer'){
        isIE = true;
    }
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf('safari')!=-1 && !(ua.indexOf('chrome')>-1)){
        isSafari = true;
    }
    isTouch = 'ontouchstart' in window;
    if(isTouch){
        iter = 25;
    }
    
    BOOKWIDTH = w;
    BOOKHEIGHT = h;
    CLIPDIFF = (BOOKHEIGHT>BOOKWIDTH)?BOOKHEIGHT/2:BOOKWIDTH/2;
    BOOKMARGINY = 10;
    BOOKMARGINX = 25;
        
    
    
    var rightW = getInt(window.getComputedStyle(d('userRight'),null).getPropertyValue("width"));
    screenW = getInt(window.getComputedStyle(d('toolBar'),null).getPropertyValue("width"));
    var diff = (screenW-(rightW+BOOKWIDTH+x+BOOKMARGINX*2))/2;
   
    if(diff>0){
        bookX = (screenW-(rightW+BOOKWIDTH+x+BOOKMARGINX*2-tBarH))/2;
    }else{
        bookX = x+BOOKMARGINX;
    }
    
    bookY = y+BOOKMARGINY;
    pw = BOOKWIDTH/2 ;
    ph = BOOKHEIGHT ;
    
    addCSS(d("main").parentNode,"background-color:"+bgColor+";");
    
    var topW = getInt(window.getComputedStyle(d('userTop'),null).getPropertyValue("width"));
    if(topW<BOOKWIDTH){
        divMove(d('userTop'),(BOOKWIDTH-topW)/2+bookX,0);   
    }else{
        divMove(d('userTop'),0,0);
    }
        
    var leftH = getInt(window.getComputedStyle(d('userLeft'),null).getPropertyValue("height"));
    if(leftH<BOOKHEIGHT){
        divMove(d('userLeft'),tBarH/2,(BOOKHEIGHT-leftH)/2+bookY);   
    }else{
        divMove(d('userTop'),tBarH/2,0);
    }
    
    var rightH = getInt(window.getComputedStyle(d('userRight'),null).getPropertyValue("height"));    
    if(rightH<BOOKHEIGHT){
        divMove(d('userRight'),tBarH/2+BOOKWIDTH+bookX+BOOKMARGINX,(BOOKHEIGHT-leftH)/2+bookY);   
    }else{
        divMove(d('userRight'),tBarH/2+BOOKWIDTH+bookX+BOOKMARGINX,0);
    }    
        
    var botW = getInt(window.getComputedStyle(d('userBottom'),null).getPropertyValue("width"));
    if(botW<BOOKWIDTH){
        divMove(d('userBottom'),(BOOKWIDTH-botW)/2+bookX,bookY+BOOKHEIGHT+tBarH);   
    }else{
        divMove(d('userBottom'),tBarH/2,bookY+BOOKHEIGHT+tBarH);
    }
    
    STX = (BOOKWIDTH/2)+bookX;
    STY = bookY;
    SBX = (BOOKWIDTH/2)+bookX;
    SBY = BOOKHEIGHT+bookY;
    
    INRAD = (BOOKWIDTH/2);
    OUTRAD = getHypot(INRAD,BOOKHEIGHT);
    
    container = d("container");
    divResize(container,BOOKWIDTH+(2*bookX),BOOKHEIGHT+y+INRAD);
    addCSS(container,"position:relative;background-color:"+bgColor+";overflow:hidden;");
    divMove(container,0,0);
    var toAdTopPad = (BOOKWIDTH-730)/2;
    var toAdBotPad = (BOOKWIDTH-300)/2;
    if(isEC){
        divMove(d("div-gpt-ad-1368107322398-1"),toAdTopPad+bookX,(bookY-90)/2);
        divMove(d("div-gpt-ad-1368107322398-0"),toAdBotPad+bookX,BOOKHEIGHT+bookY+(BOOKMARGINY*2));
    }
    
    bookDiv = createDiv("bookDiv");
    addCSS(bookDiv,"background-color:"+boardColor+";position:absolute;");
    divMove(bookDiv,bookX,bookY-BOOKMARGINY);
    divResize(bookDiv,BOOKWIDTH+1,BOOKHEIGHT+(BOOKMARGINY*2));
    
    leftDiv = createDiv("leftDiv");    
    addCSS(leftDiv,"position:relative;background-color:"+boardColor+";");
    addCSS(leftDiv,"overflow:hidden;margin-top:"+BOOKMARGINY+"px;");
    addCSS(leftDiv,"-webkit-border-top-right-radius: 9px;-moz-border-radius-topright: 9px;border-top-right-radius: 9px; border-bottom-right-radius:9px")
    divResize(leftDiv,(BOOKWIDTH/2),BOOKHEIGHT);
    
    rightDiv = createDiv("rightDiv");
    addCSS(rightDiv,"position:relative;overflow:hidden;");
    addCSS(rightDiv,"border-left:solid; border-width:1px;border-color:"+bgColor+";");
    addCSS(rightDiv,"-webkit-border-top-left-radius: 9px;-moz-border-radius-topleft: 9px;border-top-left-radius: 9px; border-bottom-left-radius:9px");
    divResize(rightDiv,(BOOKWIDTH/2),BOOKHEIGHT);
    
    if(isAjax){
        getPage(rightDiv,1);
    // rightDiv.innerHTML = jpedalDiv.innerHTML;
    }else{
        for(var z=1;z<=pageCount;z++){
            pageDom[z] = d('page'+z).innerHTML;
            d('page'+z).innerHTML="";
        }
        getPage(rightDiv,1);
    }
    divMove(rightDiv,(BOOKWIDTH/2),-BOOKHEIGHT);
    bookDiv.appendChild(rightDiv);
    bookDiv.insertBefore(leftDiv,rightDiv);
    container.appendChild(bookDiv);
    
    leftSlides = createDiv("leftSlides");
    addCSS(leftSlides,"position:absolute;background-color:"+boardColor+";z-index:7;");
    addCSS(leftSlides,"-webkit-border-top-left-radius: 9px;-moz-border-radius-topleft: 9px;border-top-left-radius: 9px; border-bottom-left-radius:9px;");
    divResize(leftSlides,BOOKMARGINX,BOOKHEIGHT+(BOOKMARGINY*2));
    divMove(leftSlides,bookX-BOOKMARGINX,bookY-BOOKMARGINY);
    container.insertBefore(leftSlides, bookDiv);
    
    rightSlides = createDiv("rightSlides");
    addCSS(rightSlides,"position:absolute;background-color:"+boardColor+";z-index:7;");
    addCSS(rightSlides,"-webkit-border-top-right-radius: 9px;-moz-border-radius-topright: 9px;border-top-right-radius: 9px; border-bottom-right-radius:9px;");
    divResize(rightSlides,BOOKMARGINX,BOOKHEIGHT+(BOOKMARGINY*2));
    divMove(rightSlides,bookX-BOOKMARGINX+BOOKWIDTH+BOOKMARGINX,bookY-BOOKMARGINY);    
    container.insertBefore(rightSlides, bookDiv);
        
    dlt = createDiv("dlt");
    dlb = createDiv("dlb");
    drt = createDiv("drt");
    drb = createDiv("drb");
    dltX = bookX;
    dltY = bookY;
    drtX = bookX+BOOKWIDTH;
    drtY = bookY;
    drbX = bookX+BOOKWIDTH;
    drbY = bookY+BOOKHEIGHT;
    dlbX = bookX;
    dlbY = bookY+BOOKHEIGHT;
    divMove(dlt,dltX,dltY);
    divMove(dlb,dlbX,dlbY-100);
    divMove(drt,drtX-100,drtY);
    divMove(drb,drbX-100,drbY-100);
    var corners = [dlt,dlb,drt,drb];
    for(var a=0;a<corners.length;a++){
        var ele = corners[a];
        if(isIE || isTouch){
            addCSS(ele,"position:absolute;background-color:pink;opacity:0.1;z-index:10;");
        }
        else{
            addCSS(ele,"position:absolute;z-index:10;background-color:pink;opacity:0.1;");
        }
        divResize(ele,100,100);
        container.insertBefore(ele,bookDiv);        
    //        bookDiv.insertBefore(ele,leftDiv);
    }   
    clipElement = createDiv("clip");
    dragElement = createDiv("drag");
    gradElement = createDiv("grad");
    dragContent = createDiv("dragContent");
    clipContent = createDiv("clipContent");
    innerClip = createDiv("innerClip");
    if(isSafari){
        addCSS(dragElement,"position:relative;z-index:5;");
    }else{
        addCSS(dragElement,"position:absolute;z-index:5;");
    }
    addCSS(clipElement,"position:absolute;background-color:"+bgColor+";overflow:hidden;z-index:6;");
    addCSS(innerClip,"position:relative;background-color:"+boardColor+";overflow:hidden;padding-top:"+BOOKMARGINY+"px;");
    addCSS(dragContent,"position:relative;background-color:white;box-shadow:-1px 1px 10px #A8A8A8;overflow:hidden;transition:left 500ms, top 500ms; -webkit-transition:left 500ms, top 500ms, -webkit-transform 500ms;");
    addCSS(dragContent,"-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px; border-radius:6px")
    divResize(clipElement,((BOOKWIDTH/2)+2*CLIPDIFF),(BOOKHEIGHT+2*CLIPDIFF));
    divResize(innerClip,(BOOKWIDTH/2),BOOKHEIGHT+BOOKMARGINY);
    divResize(dragElement,(BOOKWIDTH/2),BOOKHEIGHT);
    divResize(dragContent,(BOOKWIDTH/2),BOOKHEIGHT);
    
    addCSS(clipContent,"background-color:white;box-shadow:-1px 1px 10px #A8A8A8;overflow:hidden;");
    divResize(clipContent,(BOOKWIDTH/2),BOOKHEIGHT);
    innerClip.appendChild(clipContent);
    
    clipElement.appendChild(innerClip);
    dragElement.appendChild(dragContent);
    addListeners();
    bookDiv.parentNode.insertBefore(clipElement,bookDiv);
    bookDiv.parentNode.insertBefore(dragElement,bookDiv);
    // bookDiv.parentNode.insertBefore(gradElement,bookDiv);
    //    rightDiv.addEventListener("click", function(){
    //        handleZoom();
    //    }, false);
    //    leftDiv.addEventListener("click", function(){
    //        handleZoom();
    //    }, false);
    makeInvisible(clipElement,false);
    makeInvisible(dragElement,false);
        
    prev = document.createElement('input');
    prev.setAttribute('type','button');
    prev.setAttribute('id','prev');
    prev.setAttribute('value','PREV'); 
    addCSS(prev,"position:absolute;background-color:"+boardColor+";color:white;font-weight:bold;");
    addCSS(prev,"-webkit-border-radius: 6px ;-moz-border-radius: 6px;border-radius: 6px;");   
    divResize(prev,100,30);
    
    next = document.createElement('input');
    next.setAttribute('type','button');
    next.setAttribute('id','next');
    next.setAttribute('value','NEXT'); 
    addCSS(next,"position:absolute;background-color:"+boardColor+";color:white;font-weight:bold;");
    addCSS(next,"-webkit-border-radius: 6px ;-moz-border-radius: 6px;border-radius: 6px;");  
    divResize(next,100,30);
    //    divMove(next,50,10);
    
    prev.onclick = prevNext;
    next.onclick = prevNext;
        
    //    var goToTxt = document.createElement("input");
    //    goToTxt.setAttribute("type","text");
    //    goToTxt.setAttribute('id','goToTxt');
    //    addCSS(goToTxt,"position:absolute;background-color:white"+";color:"+boardColor+"white;font-weight:bold; text-align: center;");
    //    addCSS(goToTxt,"-webkit-border-radius: 1px ;-moz-border-radius: 1px;border-radius:1px;");  
    //    divResize(goToTxt,60,25);
    //    
    //    goToTxt.onclick = hideShare;
    
    var goBtn = document.createElement("select");
    goBtn.setAttribute('id','goBtn');
    var pageIter =pageCount/2; 
    var values = "";
    
    for(var p=0;p<=pageIter;p++){
        var pString;
        if(p==0){
            pString = 1;
        }
        else if(p==pageIter){
            pString = p*2;
        }else{
            pString = p*2+"-"+(p*2+1);
        }
        values = values+"<option value="+pString+">"+pString+"</option>";
    }
    goBtn.innerHTML = values;
    goBtn.setAttribute("onchange", "goToPage()");
    addCSS(goBtn,"position:absolute;background-color:"+boardColor+";color:white;font-weight:bold;text-align:center");
    addCSS(goBtn,"-webkit-border-radius: 6px ;-moz-border-radius: 6px;border-radius: 6px;");   
    divResize(goBtn,100,30);
    
    var zoomDiv = createDiv("zoomDiv");
    addCSS(zoomDiv,"position:absolute;color:white;font-weight:bold;");
    addCSS(zoomDiv,"-webkit-border-radius: 6px ;-moz-border-radius: 6px;border-radius: 6px;");
    
    var zoomPlus = document.createElement("input");
    zoomPlus.setAttribute("type","button");
    zoomPlus.setAttribute('id','zoomPlus');
    zoomPlus.setAttribute('value','+');
    zoomPlus.setAttribute("onclick", "zoomCtrl('plus')");
    addCSS(zoomPlus,"background-color:"+boardColor+";color:white;font-weight:bold;font-size:18px;");
    addCSS(zoomPlus,"-webkit-border-top-right-radius: 9px;-moz-border-radius-topright: 9px;border-top-right-radius: 9px; border-bottom-right-radius:9px")
    divResize(zoomPlus,30,30);
    
    var zoomMinus = document.createElement("input");
    zoomMinus.setAttribute("type","button");
    zoomMinus.setAttribute('id','Minus');
    zoomMinus.setAttribute('value','-');
    zoomMinus.setAttribute("onclick", "zoomCtrl('minus')");
    addCSS(zoomMinus,"background-color:"+boardColor+";color:white;font-weight:bold;font-size:18px;");
    addCSS(zoomMinus,"-webkit-border-top-left-radius: 9px;-moz-border-radius-topleft: 9px;border-top-left-radius: 9px; border-bottom-left-radius:9px");
    divResize(zoomMinus,30,30);
    
    var reset = document.createElement("input");
    reset.setAttribute("type","button");
    reset.setAttribute('id','reset');
    reset.setAttribute('value','zoom');
    reset.setAttribute("onclick", "zoomCtrl('reset')");
    reset.setAttribute('disabled','disabled');
    addCSS(reset,"background-color:"+boardColor+";color:white;");
    divResize(reset,50,30);
    
    zoomDiv.appendChild(zoomMinus);
    zoomDiv.appendChild(reset);
    zoomDiv.appendChild(zoomPlus);
            
    var shareBtn = document.createElement("input");
    shareBtn.setAttribute("type","button");
    shareBtn.setAttribute('id','shareBtn');
    shareBtn.setAttribute('value','Share Page');
    addCSS(shareBtn,"position:absolute;width:110px;height:30px;background-color:"+boardColor+";color:white;");
    addCSS(shareBtn,"-webkit-border-radius: 6px ;-moz-border-radius: 6px;border-radius: 6px;");  
    
    shareBtn.onclick = handleShareMenu;
    
    var tweetDiv = createDiv("tweetDiv");    
    addCSS(tweetDiv,"display:block;");
    
    var facebookDiv = createDiv("facebookDiv");
    addCSS(facebookDiv,"display:block;");
    
    var googleDiv = createDiv("googleDiv");
    addCSS(googleDiv,"display:block");
    
    var linkedDiv = createDiv("linkedDiv");
    addCSS(linkedDiv,"display:block;");
    linkedDiv.innerHTML = "<a><img src='http://www.jpedal.org/suda/icons/bLinkedIn.jpg'> linkedin</a>";
    
    var shareMenu = createDiv("shareMenu");
    addCSS(shareMenu,"position:absolute;width:60px;background-color:#E8E8E8;font-weight:bold;width:150px;padding:5px;opacity:0.9;display:none;");
    addCSS(shareMenu,"border-style:solid;border-color:white; border-width:1px; -webkit-border-radius:6px; -moz-border-radius: 6px;border-radius: 6px;");
    
        
    shareMenu.appendChild(tweetDiv);
    shareMenu.appendChild(facebookDiv);
    shareMenu.appendChild(googleDiv);
    shareMenu.appendChild(linkedDiv);
    
    
    divMove(goBtn,120,0);
    divMove(zoomDiv,240,0);
    
    divMove(shareBtn,370,0);
    divMove(shareMenu,370,28);
    divMove(next,500,0);
    
    var menuBar = createDiv('menuBar');
    menuBar.appendChild(prev);
    menuBar.appendChild(goBtn);
    menuBar.appendChild(zoomDiv);
    menuBar.appendChild(next);
    menuBar.appendChild(shareBtn);
    menuBar.appendChild(shareMenu);
    
    
    addCSS(menuBar,"position:absolute;z-index:20;font-family:Arial;font-size:14px;");
    if(BOOKWIDTH<600){
        divMove(menuBar,bookX,bookY-tBarH);
    }
    else{
        divMove(menuBar,bookX+(BOOKWIDTH-600)/2,bookY-tBarH);
    }
    divResize(menuBar,510,30);
    
    var tw = getInt(window.getComputedStyle(menuBar,null).getPropertyValue("height"));   
    container.appendChild(menuBar);
    document.onkeydown = keyNav;

    updateSlides();
    updateTweet();
    getJumpQuery();
    
    window.setTimeout(function(){
        doNextXpages(2,2+nextX);
		runAdjustSpacing();
    },1);
    
    if(isAnalytics && _gaq!=null){
        var curView = aPrefix+"/"+(curRP-1)+(curRP<=pageCount?("-"+curRP):"");
        _gaq.push(['_trackPageview', curView]);
    }
    
    document.onclick = function(e){
        var target = e.target != null ? e.target : e.srcElement;
        if(target.id!="shareBtn"){
            d('shareMenu').style.display = "none";
        }
    }
//    updatePageDom(2)
}

function hideShare(){
    d('shareMenu').style.display = "none";
}

function handleShareMenu(e){
    if(window.getComputedStyle(d('shareMenu'),null).getPropertyValue("display")=="none"){
        d('shareMenu').style.display = "block";
    }else{
        d('shareMenu').style.display = "none";
    }
}

function goToPage(){
    hideShare()
    curRP = (d('goBtn').selectedIndex)*2+1;
    curRP = curRP - 2;
    updateDragClip("plus");
    updateMethod("plus");
    updateSlides();
}

function updateTweet(){
    var tweetURL = document.URL;
    if(tweetURL.toString().indexOf("?page")>-1){
        tweetURL = tweetURL.substr(0,tweetURL.toString().indexOf("?page"));
    }  
    tweetURL = tweetURL+"?page="+curRP;
          
    var status = encodeURIComponent("" + tweetURL);
    var ecStatus = encodeURIComponent("I am reading Magazine " + tweetURL);
     
    d('tweetDiv').innerHTML = "<a href='http://twitter.com/home?status="+ecStatus+"' target='_blank' class='twitter-share-button'> <img src='http://www.jpedal.org/suda/icons/bTwitter.png' style='border:none'/> Twitter</a>"
     
    d('facebookDiv').innerHTML = "<a href='http://www.facebook.com/sharer/sharer.php?u="+status+"' target='_blank'><img src='http://www.jpedal.org/suda/icons/bFacebook.jpg'> Facebook</a>";

    d('linkedDiv').innerHTML = "<a href='http://www.linkedin.com/shareArticle?mini=true&url="+status+"&title=Enterpreneur Country Magazine' target='_blank'><img src='http://www.jpedal.org/suda/icons/bLinkedIn.jpg'> LinkedIn</a>"
     
    d('googleDiv').innerHTML = "<a href='https://plus.google.com/share?url="+status+"' target='_blank'><img src='http://www.jpedal.org/suda/icons/bGoogle.jpg'> Google+</a>"

}

function getJumpQuery(){
    var doc = document.URL;
    var jumper = "?page=";
    var jumIdx = doc.toString().indexOf(jumper);
   
    if(jumIdx>-1){
        var p = parseInt((doc.substr(jumIdx+jumper.length, doc.length)));
        if(p>2){            
            if(p>=pageCount){
                curRP = pageCount%2==0?pageCount-1:pageCount-2;
            }else{
                curRP = p%2==0?p-1:p-2;
            }
            updateDragClip("plus");
            updateMethod("plus");
            updateSlides();
        }else if(p<=2){
            Location.reload();
        }else{
            alert("invalid page number");
        }
    }    
}

function prevNext(e){
    hideShare()
    var elem, evt = e ? e:event;
    if (evt.srcElement)  elem = evt.srcElement;
    else if (evt.target) elem = evt.target;
    var temp = createDiv('temp') ;
    if(elem.value=='NEXT'){
        if(canMove("plus")){
            setDragBox(2);
            swipePages(e,true);
        }        
    }else{
        if(canMove("minus")){
            setDragBox(1);
            swipePages(e,true);
        }
    }
}

function updateSlides(){
    rightSlides.innerHTML="";
    leftSlides.innerHTML="";
    var rc = 10;
    var lc = curRP-3;
    if((pageCount-curRP)<10){
        rc = pageCount-curRP-1;
    }    
    var dataR = "";
    var dataL = "";
    for(var z=0;z<rc;z++){
        dataR=dataR+"<div style='position:absolute;width:1px;height:"+(BOOKHEIGHT-(z*2))+"px;top:"+(z+BOOKMARGINY)+"px;left:"+(z*2)+"px;background-color:white;'></div>";
    }
    if(curRP>10){
        lc = 10;
    }
    for(var q=0;q<lc;q++){
        dataL=dataL+"<div style='position:absolute;width:1px;height:"+(BOOKHEIGHT-(q*2))+"px;top:"+(q+BOOKMARGINY)+"px;left:"+(BOOKMARGINX-(q*2))+"px;background-color:white;'></div>";
    }
    d('goBtn').selectedIndex = (curRP/2);
    rightSlides.innerHTML=dataR;
    leftSlides.innerHTML=dataL;
}

function zoomCtrl(method){
    //    divOriginate(container,bookX+(BOOKWIDTH/2), 0);
   
    var divs = new Array("userTop","userBottom","userLeft","userRight");
    if(method=="plus"){
        for(var a=0;a<divs.length;a++){
            makeDisplay(d(''+divs[a]),false);
        }
        zoomRate = zoomRate+0.1;        
        totalWidth = (BOOKWIDTH+BOOKMARGINX*2)*zoomRate;
        if(totalWidth>screenW){
            divOriginate(container,0, 0);
            divMove(d("container"),-(bookX*zoomRate),(-bookY+tBarH)*zoomRate); 
        }else{            
            divOriginate(container,bookX+(BOOKWIDTH/2),0);
            divMove(d("container"),0,(-bookY+tBarH)*zoomRate); 
        }      
        d('reset').value="reset";
        d('reset').removeAttribute("disabled");
    }
    else if(method=="minus"){
        d('reset').value="reset";
        d('reset').removeAttribute("disabled");
        for(var a=0;a<divs.length;a++){
            makeDisplay(d(''+divs[a]),false);
        }
        zoomRate = zoomRate-0.1;
        totalWidth = (BOOKWIDTH+BOOKMARGINX*2)*zoomRate;
        if(totalWidth>screenW){
            divOriginate(container,0, 0);
            divMove(d("container"),-(bookX*zoomRate),(-bookY+tBarH)*zoomRate); 
        }else{            
            divOriginate(container,bookX+(BOOKWIDTH/2),0);
            divMove(d("container"),0,(-bookY+tBarH)*zoomRate); 
        }       
        d('reset').value="reset";
        d('reset').removeAttribute("disabled");
    }
    else{//for resetting
        zoomRate = 1;
        d('reset').value="zoom";
        d('reset').setAttribute("disabled", "disabled");
        for(var z=0;z<divs.length;z++){
            makeDisplay(d(''+divs[z]),true);
        }
        divMove(d("container"),0,0);
    }
    zoomIn(d("container"),zoomRate);
}

function handleZoom(){
    hideShare()
    var divs = new Array("userTop","userBottom","userLeft","userRight");
    divOriginate(container, 0, 0);
    
    var imgs = document.getElementsByTagName("img");
   
    for(var a=0;a<imgs.length;a++){
        addCSS(imgs[a],"image-rendering: optimizeQuality;-ms-interpolation-mode: bicubic;");
    }
    
    if(isZoomed){
        for(var z=0;z<divs.length;z++){
            makeDisplay(d(''+divs[z]),true);
        }
        divMove(d("container"),0,0);
        zoomIn(d("container"),1);
        if(isEC){
            makeAdDisplay(d('div-gpt-ad-1368107322398-1'), true);
            makeAdDisplay(d('div-gpt-ad-1368107322398-0'), true);
        }
        isZoomed = false;
    }else{
        for(var a=0;a<divs.length;a++){
            makeDisplay(d(''+divs[a]),false);
        }
        divMove(d("container"),-(bookX*1.4)+tBarH,-(bookY*1.4)+tBarH*1.4);
        zoomIn(d("container"),1.4);
        if(isEC){            
            makeAdDisplay(d('div-gpt-ad-1368107322398-1'), false);
            makeAdDisplay(d('div-gpt-ad-1368107322398-0'), false);
        }
        isZoomed = true;
    }
}
function addListeners(){
    var drags = new Array(dlt,drt,drb,dlb);
    if(isTouch){
        for(var z=0;z<drags.length;z++){
            drags[z].addEventListener("touchstart",function(e){
                e.preventDefault();
                touchStartX = getScrollMX(e);
                touchStartY = getScrollMY(e);
                initDragDrop(e);
            },false);
            drags[z].addEventListener("touchmove",function(e){
                movePages(e);
            },false);
            drags[z].addEventListener("touchend",function(e){
                swipePages(e,true);
            },false);
            drags[z].addEventListener("click",function(e){
                swipePages(e,true)
            },false);
        }
    }
    else{
        for(var q=0;q<drags.length;q++){
            drags[q].addEventListener("mousedown",function(e){
                initDragDrop(e);
            },false);
        }
    }
    for(var i=0;i<drags.length;i++){
        drags[i].addEventListener("click",function(e){
            swipePages(e,true)
        },false);
        drags[i].addEventListener("dblclick",function(e){
            swipePages(e,true)
        },false);
    }
}
function removeListeners(){
    dlt.onclick = null;
    drt.onclick = null;
    drb.onclick = null;
    dlb.onclick = null;
}
function initDragDrop(e){
    var boxNum = 0;
    if (e == null) {
        e = window.event;
    }
    var target = e.target != null ? e.target : e.srcElement;
    var id = target.getAttribute("id")
    if(id=="dlt"){
        boxNum = 1;
    }
    else if (id=="drt"){
        boxNum = 2;
    }
    else if (id=="drb"){
        boxNum = 3;
    }
    else{
        boxNum = 4;
    }
    if(isTouch){
    }else{
        document.onmousedown = turnStart;
        document.onmouseup = turnEnd;
    }
    currentDrag = boxNum;
    setDragBox(boxNum);
// }
}
function turnStart(e){
    // IE is retarded and doesn't pass the event object
    if (e == null) {
        e = window.event;
    }
    // IE uses srcElement, others use target
    var target = e.target != null ? e.target : e.srcElement;
    // for IE, left click == 1 for firefox left click == 0
    if ((e.button == 1 && window.event != null || e.button == 0) )
    {
        document.onmousemove = onMouseMove;
        document.body.focus();
        // prevent IE from trying to drag an image
        target.ondragstart = function() {
            return false;
        };
        return false;
    }
}
function updateDragClip(method){
    if(method=="plus"){
        if((curRP+1)>=1 && (curRP+1)<=pageCount){
            getPage(dragContent,curRP+1);
        }
        else{
            dragContent.innerHTML = "";
            dragContent.style.backgroundColor="white";
        }
        if((curRP+2)>=1 && (curRP+2)<=pageCount){
            getPage(clipContent,curRP+2);
        }else{
            clipContent.innerHTML = "";
            clipContent.style.backgroundColor=boardColor;
        }
    }else{
        if(curRP>=3){
            getPage(dragContent,curRP-2);
            if(curRP>3){
                getPage(clipContent,curRP-3);
            }else{
                clipContent.style.backgroundColor=boardColor;
                clipContent.innerHTML = "";
            }
        }
    }    
}
function exchange(from,to){
    if(from!=null && to!=null){
        to.innerHTML = from.innerHTML;
    }
}

function doNextXpages(start,end){    
    if(start>pageCount){
        return;
    }
    if(end>pageCount){
        end = pageCount
    }
    var ele = createDiv('ele');
    for(var z=start;z<=end;z++){         
        getPage(ele,z);        
    }
    ele.innerHTML = "";    
}

function canTurnPlus(){
    var canTurn = true;
    var isTotalEven = pageCount%2 == 0;
    if((!isTotalEven && curRP==pageCount) || (isTotalEven && (curRP-1)==pageCount)) {
        canTurn = false;
    }    
    return canTurn;
}

function canTurnMinus(){
    var canTurn = true;
    if(curRP==1) {
        canTurn = false;
    }    
    return canTurn;
}


function runAdjustSpacing() {
	if (adjustSpacing) {
		for (var z = 0; z < 2; z++) {
			var func = (curRP - z);
			var doc = document.getElementById('ld' + func);
			if (doc != null) {
				window.eval(doc.innerHTML);
				window['load' + func]();
			}
		}
	}
}

function updateMethod(method){

	if(googletag != null && googletag.pubads != undefined && !isZoomed) {
		googletag.cmd.push(googletag.pubads().refresh());
    }    
    var isTotalEven = pageCount%2 == 0;
    if(curRP==1){
        leftDiv.style.backgroundColor=boardColor;
    }
    if(method=="plus"){        
        doNextXpages(curRP+nextX,curRP+nextX+2);        
        if(!canTurnPlus()){
            return;
        }
        if(curRP>2){
            leftDiv.style.backgroundColor="white";
        }
        window.setTimeout(function(){         
           
            if(isIE){
                leftDiv.innerHTML = "";
                rightDiv.innerHTML = "";
                getPage(leftDiv,curRP+1);
                getPage(rightDiv,curRP+2);
            }else{
                exchange(dragContent,leftDiv);
                exchange(clipContent,rightDiv);
            }
            if(isTotalEven){
                if((curRP+2)==pageCount){
                    rightDiv.innerHTML = "";
                    rightDiv.style.backgroundColor=boardColor;
                }
            }else{
                if((curRP+1)==pageCount){
                    rightDiv.innerHTML = "";
                    rightDiv.style.backgroundColor=boardColor;
                }
            }
            makeInvisible(clipElement,false);
            makeInvisible(dragElement,false);
            dragContent.innerHTML="";
            clipContent.innerHTML="";                       
            
            curRP = curRP+2;
            //commente out code for load
			runAdjustSpacing();
			updateSlides();
            updateTweet();
                      
        }, 1);
    }else{
        
        window.setTimeout(function(){            
           
            if (isIE) {
                leftDiv.innerHTML = "";
                rightDiv.innerHTML = "";
                getPage(leftDiv,curRP-3);
                getPage(rightDiv,curRP-2);
            }else{
                exchange(clipContent,leftDiv);
                exchange(dragContent,rightDiv);
            }
           
            makeInvisible(clipElement,false);
            makeInvisible(dragElement,false);
            dragContent.innerHTML="";
            clipContent.innerHTML="";
            
            curRP=curRP-2;
			runAdjustSpacing();
           
            if(curRP==1){
                leftDiv.innerHTML = "";
                leftDiv.style.backgroundColor=boardColor;
            }
            
            updateSlides();
            updateTweet();
        }, 1);
    }
    window.setTimeout(function (){
        isTurning = false;
        prev.value = "PREV";
        next.value = "NEXT";
        makeNavBtnEnable();
        if(isAnalytics){
            var curView = aPrefix+"/"+(curRP-1)+(curRP<=pageCount?("-"+curRP):"");
            _gaq.push(['_trackPageview', curView]);
        }
    }, 200);
     
    
}

function makeNavBtnDisable(){
    prev.setAttribute("disabled","disabled");
    next.setAttribute("disabled","disabled");
}

function makeNavBtnEnable(){
    prev.removeAttribute("disabled");
    next.removeAttribute("disabled");
}

function getPage(ele,pageNum){
    if(pageNum>pageCount || pageNum<1){
        ele.innerHTML ="";
        return;
    }
    var page = pageDom[pageNum];
    if(page!=null){
        ele.innerHTML = page.innerHTML;
    }
    else if(isAjax){
        window.setTimeout(function(){
            var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest : new ActiveXObject("Microsoft.XMLHTTP");
            xmlhttp.onreadystatechange=function(){
                if (xmlhttp.readyState==4 && xmlhttp.status==200){
                    ele.innerHTML= xmlhttp.responseText;
                    var pg = createDiv("page"+pageNum);
                    pg.innerHTML = xmlhttp.responseText;                
                    pageDom[pageNum] = pg;
                }
            }
            var pStr = pageURL(pageNum,pageCount);
            xmlhttp.open("GET",pStr+".html",false);
            // xmlhttp.setRequestHeader("Content-Type", "text/html; charset=utf-8");
            xmlhttp.send();
        }, 1);       
    }else{
        ele.innerHTML = "";
    }
}

function getViewStr(){
    if(curRP==1){
        return "1";
    }
    else{
        return (curRP-1)+"-"+(curRP);
    }
}

function updatePageDom(num){
    if(num>pageCount){
        return;
    }
    var page = d('page'+num);
    if(page==null){
        var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest : new ActiveXObject("Microsoft.XMLHTTP");
        xmlhttp.onreadystatechange=function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                // if(num<=pageCount){
                // window.setTimeout(function(){
                var pg = createDiv("page"+num);
                pg.innerHTML = xmlhttp.responseText;
                var pg1 = d("page1");
                //                jpedalDiv.insertBefore(pg,pg1);
                num++;
                window.setTimeout(function (){
                    updatePageDom(num);
                }, 200);
            // },10)
            // }
            }
        }
        var pStr = pageURL(num,pageCount);
        xmlhttp.open("GET",pStr+".html",false);
        // xmlhttp.setRequestHeader("Content-Type", "text/html; charset=utf-8");
        xmlhttp.send();
    }
}
function handleDRB(mx,my){
    var moveX = -1;
    var moveY = -1;
    var bmx = mx-bookX;
    var bmy = my-bookY;
    var dx = SBX - mx;
    var dy = SBY - my;
    var a2f = Math.atan2(dy, dx);
    var radX = SBX - Math.cos(a2f) * INRAD;
    var radY = SBY - Math.sin(a2f)* INRAD;
    var lenFromSB = Math.sqrt( (dy*dy) + ( dx*dx));
    dx = STX - mx;
    dy = STY - my;
    a2f = Math.atan2(dy, dx);
    radX = STX - Math.cos(a2f) * OUTRAD;
    radY = STY - Math.sin(a2f)* OUTRAD;
    var lenFromST = Math.sqrt( (dy*dy) + ( dx*dx));
    // if(lenFromST>OUTRAD) {// && bmx>0 && bmy<BOOKWIDTH){
    // moveX = radX;
    // moveY = radY;
    // }
    //
    if(lenFromSB<=INRAD && lenFromST<=OUTRAD ){
        moveX = mx;
        moveY = my;
    }
    if(moveX>=0 && moveY>=0){
        var baseSA = getInt(SBY-my);
        var baseSB = getInt(SBX+INRAD-mx);
        var baseSC = getInt(getHypot(baseSA,baseSB));
        var oppAdj = baseSA/baseSB;
        var eb = Math.atan(oppAdj);
        var be = Math.tan(eb);
        var cricB = be * (baseSC/2);
        var cricA = (baseSC/2);
        var cricC = getHypot(cricB,cricA);
        var test = getDegree(Math.atan(cricA/cricB))
        var toRot = test;
        var dragAng = 180-(2*toRot);
        var clipAng = test>=0? (90-toRot) : -(90+toRot);
        var yy = CLIPDIFF-BOOKMARGINY;
        var xx = -pw+getInt(cricC);
        var clipNX = (yy*Math.sin(clipAng*Math.PI/180)+ xx*Math.cos(clipAng*Math.PI/180));//(clipAng*3.1);
        var clipNY = (yy*Math.cos(clipAng*Math.PI/180)- xx*Math.sin(clipAng*Math.PI/180));
        if(bmx>=0){
            divOriginate(dragElement,0,getInt(dragElement.style.height));
            divOriginate(clipElement,0,BOOKHEIGHT+CLIPDIFF);
            divOriginate(innerClip,0,BOOKHEIGHT+CLIPDIFF);
            divTurn(dragElement,moveX,moveY-BOOKHEIGHT,dragAng);
            divTurn(clipElement,(SBX-cricC)+pw,STY-CLIPDIFF,clipAng);
            divTurn(innerClip,clipNX,clipNY,-clipAng);
            makeInvisible(clipElement,true);
            makeInvisible(dragElement,true);
        }
    }
}
function handleDLB(mx,my){
    var moveX = -1;
    var moveY = -1;
    var bmx = mx-bookX;
    var bmy = my-bookY;
    var dx = mx-SBX;
    var dy = SBY - my;
    var a2f = Math.atan2(dy, dx);
    var radX = SBX - Math.cos(a2f) * INRAD;
    var radY = SBY - Math.sin(a2f)* INRAD;
    var lenFromSB = Math.sqrt( (dy*dy) + ( dx*dx));
    dx = STX - mx;
    dy = STY - my;
    a2f = Math.atan2(dy, dx);
    radX = STX - Math.cos(a2f) * OUTRAD;
    radY = STY - Math.sin(a2f)* OUTRAD;
    var lenFromST = Math.sqrt( (dy*dy) + ( dx*dx));
    // if(lenFromST>OUTRAD && bookMX>0 && bookMX<BOOKWIDTH){
    // moveX = radX;
    // moveY = radY;
    // }
    if(lenFromSB<INRAD && lenFromST<OUTRAD ){
        moveX = mx;
        moveY = my;
    }
    if(moveX>=0 && moveY>=0){
        var baseSA = getFloat(SBY-my);
        var baseSB = getFloat(mx-bookX);
        var baseSC = getFloat(getHypot(baseSA,baseSB));
        var oppAdj = baseSA/baseSB;
        var eb = Math.atan(oppAdj);
        var be = Math.tan(eb);
        var cricB = be * (baseSC/2);
        var cricA = (baseSC/2);
        var cricC = getHypot(cricB,cricA);
        var test = getDegree(Math.atan(cricA/cricB))
        var toRot = test;
        var innerClipX = (pw+CLIPDIFF*2);
        var clipAng = test>=0? (90-toRot) : -(90+toRot);
        var dragAng = -(180-(2*toRot));
        var yy =CLIPDIFF-BOOKMARGINY;
        var xx =-innerClipX+getInt(cricC);
        var clipNX = (yy*Math.sin(clipAng*Math.PI/180)+ xx*Math.cos(clipAng*Math.PI/180));//(clipAng*3.1);
        var clipNY = (yy*Math.cos(clipAng*Math.PI/180)- xx*Math.sin(clipAng*Math.PI/180));
        if(bmx>=0){
            divOriginate(dragElement,BOOKWIDTH/2,BOOKHEIGHT);
            divOriginate(clipElement,getFloat(clipElement.style.width),BOOKHEIGHT+CLIPDIFF);
            divOriginate(innerClip,innerClipX,BOOKHEIGHT+CLIPDIFF);
            divTurn(dragElement,bookX+bmx-(BOOKWIDTH/2),bookY+bmy-BOOKHEIGHT,dragAng);
            divTurn(clipElement,cricC+bookX-getFloat(clipElement.style.width),STY-CLIPDIFF,-(clipAng));
            divTurn(innerClip,-clipNX,clipNY,clipAng);
            makeInvisible(clipElement,true);
            makeInvisible(dragElement,true);
        }
    }
}
function handleDLT(mx,my){
    var moveX = -1;
    var moveY = -1;
    var bmx = mx-bookX;
    var bmy = my-bookY;
    var dx = STX-mx;
    var dy = my-STY;
    var a2f = Math.atan2(dy, dx);
    var radX = SBX - Math.cos(a2f) * INRAD;
    var radY = SBY - Math.sin(a2f)* INRAD;
    var lenFromSB = Math.sqrt( (dy*dy) + ( dx*dx));
    dx = SBX - mx;
    dy = SBY - my;
    a2f = Math.atan2(dy, dx);
    radX = STX - Math.cos(a2f) * OUTRAD;
    radY = STY - Math.sin(a2f) * OUTRAD;
    var lenFromST = Math.sqrt( (dy*dy) + ( dx*dx));
    if(lenFromSB<INRAD && lenFromST<OUTRAD ){
        moveX = mx;
        moveY = my;
    }
    if(moveX>=0 && moveY>=0){
        var baseSA = getFloat(my-STY);
        var baseSB = getFloat(mx-bookX);
        var baseSC = getFloat(getHypot(baseSA,baseSB));
        var oppAdj = baseSA/baseSB;
        var eb = Math.atan(oppAdj);
        var be = Math.tan(eb);
        var cricB = be * (baseSC/2);
        var cricA = (baseSC/2);
        var cricC = getHypot(cricB,cricA);
        var test = getDegree(Math.atan(cricA/cricB))
        var toRot = test;
        var innerClipX = (pw+CLIPDIFF*2);
        var clipAng = test>=0? (90-toRot) : -(90+toRot);
        var dragAng = (180-2*toRot);
        ;
        var yy = -CLIPDIFF+BOOKMARGINY;
        var xx =-innerClipX+getInt(cricC);
        var clipNX = (yy*Math.sin(clipAng*Math.PI/180)+ xx*Math.cos(clipAng*Math.PI/180));//(clipAng*3.1);
        var clipNY = (yy*Math.cos(clipAng*Math.PI/180)- xx*Math.sin(clipAng*Math.PI/180));
        if(bmx>=0){
            divOriginate(dragElement,pw,0);
            divOriginate(clipElement,getFloat(clipElement.style.width),CLIPDIFF);
            divOriginate(innerClip,getFloat(clipElement.style.width),CLIPDIFF);
            divTurn(dragElement,mx-pw,my,dragAng);
            divTurn(clipElement,cricC+bookX-innerClipX,STY-CLIPDIFF,clipAng);
            divTurn(innerClip,-clipNX,-clipNY,-clipAng);
            makeInvisible(clipElement,true);
            makeInvisible(dragElement,true);
        }
    }
}
function handleDRT(mx,my){
    var bmx = mx-bookX;
    var bmy = my-bookY;
    var moveX = -1;
    var moveY = -1;
    var dx = STX-mx;
    var dy = my-STY;
    var a2f = Math.atan2(dy, dx);
    var radX = SBX - Math.cos(a2f) * INRAD;
    var radY = SBY - Math.sin(a2f) * INRAD;
    var lenFromSB = Math.sqrt( (dy*dy) + ( dx*dx));
    dx = SBX - mx;
    dy = SBY - my;
    a2f = Math.atan2(dy, dx);
    radX = STX - Math.cos(a2f) * OUTRAD;
    radY = STY - Math.sin(a2f) * OUTRAD;
    var lenFromST = Math.sqrt( (dy*dy) + ( dx*dx));
    // if(lenFromST>OUTRAD && bookMX>0 && bookMX<BOOKWIDTH){
    // moveX = radX;
    // moveY = radY;
    // }
    if(lenFromSB<INRAD && lenFromST<OUTRAD ){
        moveX = mx;
        moveY = my;
    }
    if(moveX>=0 && moveY>=0){
        var baseSA = getInt(STY-my);
        var baseSB = getInt(SBX+INRAD-mx);
        var baseSC = getInt(getHypot(baseSA,baseSB));
        var oppAdj = baseSA/baseSB;
        var eb = Math.atan(oppAdj);
        var be = Math.tan(eb);
        var cricB = be * (baseSC/2);
        var cricA = (baseSC/2);
        var cricC = getHypot(cricB,cricA);
        var test = getDegree(Math.atan(cricA/cricB))
        var toRot = test;
        var dragAng = (180-2*toRot);
        var yy = CLIPDIFF-BOOKMARGINY;
        var xx = -pw+getInt(cricC);
        var clipAng = test<=0?(-90-toRot):90-toRot;
        var clipNX = (yy*Math.sin(clipAng*Math.PI/180)+ xx*Math.cos(clipAng*Math.PI/180));//(clipAng*3.1);
        var clipNY = (yy*Math.cos(clipAng*Math.PI/180)- xx*Math.sin(clipAng*Math.PI/180));
        if(bmx>=0){
            divOriginate(dragElement,0,0);
            divOriginate(clipElement,0,CLIPDIFF);
            divOriginate(innerClip,0,CLIPDIFF);
            divTurn(dragElement,moveX,moveY,dragAng);
            divTurn(clipElement,(SBX-cricC)+pw,STY-CLIPDIFF,clipAng);
            divTurn(innerClip,clipNX,clipNY,-clipAng);
            makeInvisible(clipElement,true);
            makeInvisible(dragElement,true);
        }
    }
}


function movePages(e){
    hideShare()
    // if(isTouch){
    // e.preventDefault();
    // }
    var mx = getScrollMX(e);
    var my = getScrollMY(e);
    switch(dragBox){
        case 1:
            if(canMove("minus")){
                handleDLT(mx,my);
            }else{
                return;
            }
            break;
        case 2:
            if(canMove("plus")){
                handleDRT(mx,my);
            }else{
                return;
            }
            break;
        case 3:
            if(canMove("plus")){
                handleDRB(mx,my);
            }else{
                return;
            }
            break;
        case 4:
            if(canMove("minus")){
                handleDLB(mx,my);
            }else{
                return;
            }
            break;
    }
}

function canMove(method){
    if(method=="plus"){
        if(pageCount%2==0){
            if(curRP+1>pageCount){                    
                return false;
            }
        }else{                
            if(curRP>=pageCount){
                return false;
            }
        }
    }else{
        if(curRP==1){
            return false;
        }
    }    
    return true;
}

function swipePages(e,isFlip){
    
    hideShare();
    if(isTouch){
        var t = e.target != null ? e.target : e.srcElement;
        if(t.getAttribute("id")=="prev" || t.getAttribute("id")=="next"){
            isFlip = true;
        }
        else if(Math.abs(lastX-touchStartX)>40 || Math.abs(lastY-touchStartY)>40){
            isFlip = false;
        }else{
            isFlip = true;
        }
    }    
    var x = new Array();
    var y = new Array();
    var xGap = 0;
    var yGap = 0;
    var count = 0 ;
    var inter;
    switch(dragBox){
        case 1:
            if(!canMove("minus")|| isTurning){
                return;
            }
            if(isTouch){
                toAnimX = lastX;
                toAnimY = lastY;
            }else{
                toAnimX = getScrollMX(e);
                toAnimY = getScrollMY(e);
            }
            toAnimX = toAnimX>bookX?toAnimX:getInt(dragElement.style.left);
            if(isFlip || toAnimX>bookX+pw){
                isTurning = true;
                makeNavBtnDisable();
                prev.value = "please wait";
                next.value = "please wait";
                xGap = (drtX+1)-toAnimX;
                yGap = toAnimY-(drtY-1) ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]+(xGap/iter);
                        y[z] = y[z-1]-(yGap/iter);
                    }
                }
                x[x.length] = drtX-1;
                y[y.length] = drtY;
                inter = window.setInterval(function(){
                    handleDLT(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        updateMethod("minus");
                        setDragBox(-1);
                        window.clearInterval(inter);
                    }
                },speed);
            }else{
                xGap = toAnimX-(dltX+1);
                yGap = toAnimY-(dltY-1) ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]-(xGap/iter);
                        y[z] = y[z-1]-(yGap/iter);
                    }
                }
                x[x.length] = dltX+1;
                y[y.length] = dltY+1;
                inter = window.setInterval(function(){
                    handleDLT(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        setDragBox(-1);
                        window.clearInterval(inter);
                    }
                },speed);
            }
            break;
        case 2:
            if(!canMove("plus") || isTurning ){
                return ;
            }
            if(isTouch){
                toAnimX = lastX;
                toAnimY = lastY;
            }else{
                toAnimX = getScrollMX(e);
                toAnimY = getScrollMY(e);
            }
            toAnimX = toAnimX>bookX?toAnimX:getInt(dragElement.style.left);
            if(isFlip || toAnimX<bookX+pw){
                isTurning = true;
                makeNavBtnDisable();
                prev.value = "please wait";
                next.value = "please wait";
                xGap = toAnimX - (dltX+1);
                yGap = toAnimY - (dltY-1) ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = xGap;
                        y[z] = yGap;
                    }
                    else{
                        x[z] = x[z-1]-xGap/iter;
                        y[z] = y[z-1]-yGap/iter;
                    }
                }
                x[x.length]=1;
                y[y.length]=1;
                inter = window.setInterval(function(){
                    handleDRT(x[count]+bookX,y[count]+bookY);
                    count++;
                    if(count+1>x.length){
                        setDragBox(-1);
                        updateMethod("plus");
                        window.clearInterval(inter);
                    }
                },speed);
            }else{
                xGap = (drtX+1)-toAnimX;
                yGap = toAnimY-(dltY-1) ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]+(xGap/iter);
                        y[z] = y[z-1]-(yGap/iter);
                    }
                }
                x[x.length] = drtX-1;
                y[y.length] = drtY+1;
                inter = window.setInterval(function(){
                    handleDRT(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        setDragBox(-1);
                        window.clearInterval(inter);
                    }
                },speed);
            }
            break;
        case 3:
            if(!canMove("plus") || isTurning){
                return ;
            }
            if(isTouch){
                toAnimX = lastX;
                toAnimY = lastY;
            }else{
                toAnimX = getScrollMX(e);
                toAnimY = getScrollMY(e);
            }
            toAnimX = e.clientX>bookX?e.clientX:getInt(dragElement.style.left);
            if(isFlip || toAnimX<bookX+pw){
                isTurning = true;
                makeNavBtnDisable();
                prev.value = "please wait";
                next.value = "please wait";
                xGap = toAnimX - (dlbX+1);
                yGap = (dlbY-1)- toAnimY ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = xGap;
                        y[z] = yGap;
                    }
                    else{
                        x[z] = x[z-1]-xGap/iter;
                        y[z] = y[z-1]-yGap/iter;
                    }
                }
                x[x.length]=0;
                y[y.length]=0;
                inter = window.setInterval(function(){
                    handleDRB(x[count]+bookX,SBY-y[count]);
                    count++;
                    if(count+1>x.length){
                        setDragBox(-1);
                        updateMethod("plus");
                        window.clearInterval(inter);
                    }
                },speed);
            }else{
                xGap = (drbX+1)-toAnimX;
                yGap = (dlbY-1)- toAnimY ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]+(xGap/iter);
                        y[z] = y[z-1]+(yGap/iter);
                    }
                }
                x[x.length] = drbX-1;
                y[y.length] = drbY-1;
                inter = window.setInterval(function(){
                    handleDRB(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        setDragBox(-1);
                        window.clearInterval(inter);
                    }
                },speed);
            }
            break;
        case 4:
            if(!canMove("minus") || isTurning){
                return;
            }
            if(isTouch){
                toAnimX = lastX;
                toAnimY = lastY;
            }else{
                toAnimX = getScrollMX(e);
                toAnimY = getScrollMY(e);
            }
            toAnimX = toAnimX>bookX?toAnimX:getInt(dragElement.style.left);
            if(isFlip || toAnimX>bookX+pw){
                isTurning = true;
                makeNavBtnDisable();
                prev.value = "please wait";
                next.value = "please wait";
                xGap = (drbX+1)-toAnimX;
                yGap = (drbY-1)- toAnimY ;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]+(xGap/iter);
                        y[z] = y[z-1]+(yGap/iter);
                    }
                }
                x[x.length] = drbX-1;
                y[y.length] = drbY-1;
                inter = window.setInterval(function(){
                    handleDLB(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        updateMethod("minus");
                        setDragBox(-1);
                        window.clearInterval(inter);
                    }
                },speed);
            }else{
                xGap = toAnimX-dlbX;
                yGap = dlbY- toAnimY;
                for(var z=0;z<iter;z++){
                    if(z==0){
                        x[z] = toAnimX;
                        y[z] = toAnimY;
                    }
                    else{
                        x[z] = x[z-1]-(xGap/iter);
                        y[z] = y[z-1]+(yGap/iter);
                    }
                }
                x[x.length] = dlbX+1;
                y[y.length] = dlbY-1;
                inter = window.setInterval(function(){
                    handleDLB(x[count],y[count]);
                    count++;
                    if(count>x.length){
                        setDragBox(-1);
                        window.clearInterval(inter);
                        prev.value = "PREV";
                        next.value = "NEXT";
                    }
                },speed);
            }
            break;
    }
//googletag.cmd.push(googletag.pubads().refresh());   
}
function onMouseMove(e){
    if (e == null) {
        var e = window.event;
    }
    movePages(e);
}
function turnEnd(e)
{
    if (dragElement != null){
        swipePages(e,false);
        document.onmousemove = null;
        document.onmouseup = null;
        document.onmousedown = null;
    }
}
//below here are the utility functions and no need to change these
function pageURL(cur,total){
    return cur;
//    if(cur==total){
//        return ""+cur;
//    }else{
//        var str = ""+cur;
//        var totStr = ""+total;
//        var pad = totStr.length - str.length;
//        for(var z=0;z<pad;z++){
//            str = "0"+str;
//        }
//        return str;
//    }
}
function d(id){
    return document.getElementById(id);
}
function getHypot(w,h){
    return Math.sqrt((w*w)+(h*h));
}
function getRadian(deg){
    return (deg*Math.PI)/180;
}
function getDegree(rad){
    return (rad/Math.PI)*180;
}
function getFloat(value){
    var n = parseFloat(value);
    return n == null || isNaN(n) ? 0.0 : n;
}
function getInt(value){
    var n = parseFloat(value);
    return n == null || isNaN(n) ? 0.0 : n;
}
function divRotate(ele,deg){
    var properties = ['transform','WebkitTransform','msTransform','MozTransform','OTransform'];
    for(var z=0;z<properties.length;z++){
        ele.style[properties[z]] = 'rotate(' + deg + 'deg)';
    }
}
function divOriginate(ele,x,y){
    ele.style.webkitTransformOrigin = x+"px "+y+"px";
    ele.style.transformOrigin = x+"px "+y+"px";
    ele.style.mozTransformOrigin = x+"px "+y+"px";
    ele.style.msTransformOrigin = x+"px "+y+"px";
    ele.style.oTransformOrigin = x+"px "+y+"px";
}
function divMove(ele,x,y){
    ele.style.top = y+"px";
    ele.style.left = x+"px";
}
function divTurn(ele,x,y,deg){
    ele.style.transform = "translate3d(" + x + "px, " + y + "px, 1px) rotate("+deg+"deg)";
    ele.style.webkitTransform = "translate3d(" + x + "px, " + y + "px, 1px) rotate("+deg+"deg)";
    ele.style.mozTransform = "translate3d(" + x + "px, " + y + "px, 1px) rotate("+deg+"deg)";
    ele.style.msTransform = "translate3d(" + x + "px, " + y + "px, 1px) rotate("+deg+"deg)";
    ele.style.oTransform = "translate3d(" + x + "px, " + y + "px, 1px) rotate("+deg+"deg)";
}
function divTranslate(ele,x,y){
    ele.style.webkitTransform = 'translate(' + x + 'px,' + y + 'px);';
    ele.style.transform ='translate(' + x + 'px,' + y + 'px);';
    ele.style.mozTransform = 'translate(' + x + 'px,' + y + 'px);';
    ele.style.msTransform = 'translate(' + x + 'px,' + y + 'px);';
    ele.style.oTransform = 'translate(' + x + 'px,' + y + 'px);';
}
function addCSS(ele,text){
    var styleAttr = ele.getAttribute("style");
    if(styleAttr!=null){
        ele.setAttribute("style",styleAttr+text);
    }else{
        ele.setAttribute("style",text);
    }
}

function makeDisplay(ele,show){
    if(show){
        ele.style.display="inherit";
    //        ele.style.visibility="visible";
    }else{
        ele.style.display="none";
    //        ele.style.visibility="hidden";
    }
}

function makeInvisible(ele,show){
    if(show){
        ele.style.display="inherit";
        ele.style.visibility="visible";
    }else{
        //         ele.style.display="none";
        ele.style.visibility="hidden";
    }
}
function makeAdDisplay(ele,show){
    if(show){
        ele.style.display="inherit";
    }else{
        ele.style.display="none";
    }
}
function divStop(ele){
    var properties = ['transform','WebkitTransform','msTransform','MozTransform','OTransform'];
    for(var z=0;z<properties.length;z++){
        ele.style[properties[z]] = 'none';
    }
}
function divResize(ele,w,h){
    ele.style.width = ""+w+"px";
    ele.style.height = ""+h+"px";
}
function divGradient(ele,color){
    ele.style.backgroundImage = "-webkit-gradient(whatever)";
}
function createDiv(id){
    var ele = document.createElement("div");
    ele.setAttribute("id",id);
    return ele;
}
function keyNav(e){
    if(e.keyCode==39){
        if(canMove("plus")){
            setDragBox(2);
            swipePages(e,true);
        } 
    }
    else if(e.keyCode==37){
        if(canMove("minus")){
            setDragBox(1);
            swipePages(e,true);
        } 
    }
}
function getScrollMX(e){
    var mx = 0;
    if(isIE){
        mx = e.clientX+ document.body.scrollLeft;
    }else{
        mx = e.clientX+ (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
    }
    if(isTouch){
        mx = e.touches[0].pageX;
        lastX = mx;
    }
    var t = e.target != null ? e.target : e.srcElement;
    if(t.getAttribute("id")=="prev"){
        return dltX+5;
    }else if(t.getAttribute("id")=="next"){
        return drtX-5;
    }
    return mx;
}
function getScrollMY(e){
    var my = 0;
    if(isIE){
        my = e.clientY+ document.body.scrollTop;
    }else{
        my = e.clientY+ (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
    }
    if(isTouch){
        my = e.touches[0].pageY;
        lastY = my;
    }
    var t = e.target != null ? e.target : e.srcElement;
    if(t.getAttribute("id")=="prev"){
        return dltY+5;
    }else if(t.getAttribute("id")=="next"){
        return drtY-5;
    }
   
    return my;
}
function zoomIn(ele,zoom){
	ele.style.transform = 'scale(' + zoom + ')';
	ele.style.MozTransform = 'scale(' + zoom + ')';
	ele.style.msTransform = 'scale(' + zoom + ')';
	ele.style.OTransform = 'scale(' + zoom + ')';
	ele.style.webkitTransform = 'scale(' + zoom + ')';

	// Make webkit redraw text to prevent blurring.
	d('main').style.display = 'none';
	d('main').offsetHeight;
	d('main').style.display = 'block';
}

function addVideoLink(x, y, width, height, container, link) {
    
    var linkEl = document.createElement("input");
    linkEl.setAttribute("type", "button");
    linkEl.setAttribute("class", "videoLink");
    linkEl.setAttribute("onclick", "openVideoLink('"+link+"');");
    if(isIE){
        addCSS(linkEl,"position:absolute; z-index:30; opacity:0.1; cursor:pointer;");
    }else{
        addCSS(linkEl,"position:absolute; z-index:30; cursor:pointer; background:none repeat scroll 0 0 transparent;");
    }
   
    divResize(linkEl, width,height);
    divMove(linkEl,x,y);
    linkEl.style.borderStyle= "none";
    // <input type="button" tabindex="4" id="form12" name="1026 0 R" pdfFieldName="1026 0 R" onclick="window.location.href='08.html';" style="cursor:pointer;" title="Page 8"/>
    container.insertBefore(linkEl,container.firstChild);
}

function addLink(x, y, width, height, container, link) {

    var linkEl = document.createElement("input");
    linkEl.setAttribute("type", "button");
    linkEl.setAttribute("class", "link");
    linkEl.setAttribute("onclick", "window.open('"+link+"', '_blank');");
    if(isIE){
        addCSS(linkEl,"position:absolute; z-index:30; opacity:0.1; cursor:pointer;");
    }else{
        addCSS(linkEl,"position:absolute; z-index:30; cursor:pointer; background:none repeat scroll 0 0 transparent;");
    }

    divResize(linkEl, width,height);
    divMove(linkEl,x,y);
    linkEl.style.borderStyle= "none";
    container.insertBefore(linkEl,container.firstChild);
}

function removeVideoLinks() {
    var videoLinks = document.getElementsByClassName("videoLink");
    for(var i=0; i < videoLinks.length; i ++) {
        var element = videoLinks.item(i);
        element.parentNode.removeChild(element);
    }
    var links = document.getElementsByClassName("link");
    for(var i=0; i < links.length; i ++) {
        var element = links.item(i);
        element.parentNode.removeChild(element);
    }
}
function openVideoLink(link) {
    //     alert("Open link: " + link);
    var container = document.getElementsByTagName("body")[0];
    var ifrm = document.createElement("iframe");
    ifrm.setAttribute("src", link);
    ifrm.setAttribute("frameborder", "0");
    ifrm.setAttribute("allowfullscreen", "");
    ifrm.setAttribute("id", "youtubeVideo");
    ifrm.style.width = 640+"px";
    ifrm.style.height = 360+"px";
    ifrm.style.position = "fixed";
    ifrm.style.margin = "-180px 0 0 -320px";
    ifrm.style.top = "50%";
    ifrm.style.left = "50%";
    var videoBg = document.createElement("div");
    videoBg.setAttribute("id", "videoOverlay");
    videoBg.setAttribute("onclick", "closeVideoLink();");
    videoBg.style.backgroundColor = "black";
    videoBg.style.opacity = 0.9;
    videoBg.style.position = "fixed";
    videoBg.style.top = "0";
    videoBg.style.left = "0";
    videoBg.style.width = "100%";
    videoBg.style.height = "100%";
    videoBg.style.zIndex = "100";
    container.insertBefore(videoBg,container.children[1]);
    // videoBg.appendChild(ifrm);
    var centredDiv = document.createElement("div");
    centredDiv.style.backgroundColor = "white";
    centredDiv.id = "videoDiv";
    centredDiv.style.margin = "-200px 0 0 -340px";
    centredDiv.style.width = 680+"px";
    centredDiv.style.height = 400+"px";
    centredDiv.style.position = "fixed";
    centredDiv.style.top = "50%";
    centredDiv.style.left = "50%";
    centredDiv.style.zIndex = "150";
    centredDiv.style.opacity = 1;
    container.insertBefore(centredDiv,container.children[1]);
    centredDiv.appendChild(ifrm);
    var closeButton = document.createElement("a");
    closeButton.innerHTML = "close";
    closeButton.setAttribute("href", "javascript:closeVideoLink();");
    closeButton.style.color = "red";
    closeButton.style.fontSize = "20px";
    closeButton.style.position = "relative";
    closeButton.style.left = 630+"px";
    closeButton.style.top = "-2px";
    closeButton.style.textDecoration = "none";
    centredDiv.insertBefore(closeButton,ifrm);
}

function closeVideoLink() {
    var el = document.getElementById("videoOverlay");
    if(el != null) {
        if(d('youtubeVideo')!=null){
            d('youtubeVideo').removeAttribute("src");
            d('youtubeVideo').innerHTML ="";
        }
        el.innerHTML = "";
        el.parentNode.removeChild(el);
        el = document.getElementById("videoDiv");
        if(el != null) {
            el.innerHTML = "";
            el.parentNode.removeChild(el);
        }
    }
}

function adjustWordSpacing(divName,actualWidth) {

    var el = document.getElementById(divName);
    var rawWidth=el.offsetWidth;
    if(actualWidth>rawWidth){
        var spacing= 0;
        var s=spacing+'px';
        el.style.wordSpacing = s;
        rawWidth=el.offsetWidth;

        var diff=rawWidth-actualWidth;
        if(diff<0)
            diff=-diff;
        var newDiff=diff-1;

        while (spacing<40 && newDiff!=diff && newDiff<diff){

            diff=newDiff;
            spacing= spacing+1;

            s=spacing+'px';
            el.style.wordSpacing = s;

            newDiff=el.offsetWidth-actualWidth;
            if(newDiff<0)
                newDiff=-newDiff;
        }
        if(diff<=newDiff)
            spacing=spacing-1;
        s=spacing+'px';
        el.style.wordSpacing =s;
    }
}
