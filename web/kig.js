/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function myFunction() {

    var date = new Date();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    if (minutes > 30)
    {
        hours = hours + 1;
    }
    var ampm = hours >= 12 ? 'pm' : 'am';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    //inutes = minutes < 10 ? '0'+minutes : minutes;
    var strTime = hours + ampm;
    var selection;
    switch (strTime)
    {
        case "1am":
            selection = 0;
            break;
        case "2am":
            selection = 1;
            break;
        case "3am":
            selection = 2;
            break;
        case "4am":
            selection = 3;
            break;
        case "5am":
            selection = 4;
            break;
        case "6am":
            selection = 5;
            break;
        case "7am":
            selection = 6;
            break;
        case "8am":
            selection = 7;
            break;
        case "9am":
            selection = 8;
            break;
        case "10am":
            selection = 9;
            break;
        case "11am":
            selection = 10;
            break;
        case "12pm":
            selection = 11;
            break;
        case "1pm":
            selection = 12;
            break;
        case "2pm":
            selection = 13;
            break;
        case "3pm":
            selection = 14;
            break;
        case "4pm":
            selection = 15;
            break;
        case "5pm":
            selection = 16;
            break;
        case "6pm":
            selection = 17;
            break;
        case "7pm":
            selection = 18;
            break;
        case "8pm":
            selection = 19;
            break;
        case "9pm":
            selection = 20;
            break;
        case "10pm":
            selection = 21;
            break;
        case "11pm":
            selection = 22;
            break;
        case "12am":
            selection = 23;
            break;
        default:
            selection = 14;
    }

    document.forms[0].hr.value = strTime;
    document.forms[0].hours.selectedIndex = selection;
    
}
function disableHrsName() {
    document.forms[0].hrsName.disabled = true;
    document.forms[0].hrsName.selectedIndex = 0;
    
}
function enableHrsName() {
    document.forms[0].hrsName.disabled = false;
    
}

function followNameSelection() {
    document.forms[0].hrsName.selectedIndex = document.forms[0].empName.selectedIndex;
}


