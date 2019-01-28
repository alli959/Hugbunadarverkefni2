let selectedButton;
let shotButton;
let shotAwayButton;
let assistButton;
let reboundButton;
let homeScoreDiv;
let awayScoreDiv;
let foulButton;
let foulDefence;
let freeThrowButt;
let howManyFT;
let freeThrowShot;
let turnoverButt;
let defensiveReb;
let missedAway;
let blockedShot;
let foulAway;
let foulAwayPlayer;
let howManyFTAway;
let freeThrowAway;
let turnoverPlayer;

let shotFrom; // Hvar leikmaður skaut
let playerId; // Leikmaðurinn sem er valin sem skýtur
let playerAssist; // Leikmaður sem gaf stoðsendingu
let playerRebound; // Leikmaður sem tekur frákast
let playerShoting;
let playerBlock;
let playerSteal;

let subId;


let scoreHome = 0;
let scoreAway = 0;

let FTshots;

let playerSelected = false;
/*
let subSelected = false;


function substitute(id){
    subId = id;
        actionPost("", "", false, "", "", subId, playerId, "", "")
}*/




function shot(val) {
   if (val == 1) { // Made
       shotButton.classList.add('hidden'); // Felur takkan
       const allButtons = assistButton.querySelectorAll('.button');
       for (let i = 0; i < allButtons.length; i++) {
           const buttVal = allButtons[i].value;
           if (buttVal == playerId){
               playerShoting = allButtons[i];
               allButtons[i].classList.add('hidden'); // Getur ekki gefið stoðsendingu á sjálfan sig
           }
       }
       assistButton.classList.remove('hidden');

       addScore(shotFrom, true);

       console.log('Made basket by player ' + playerId);

   } else { // Missed
       shotButton.classList.add('hidden'); // Felur takkan
       reboundButton.classList.remove('hidden');
       console.log('Missed basket by player ' + playerId);

   }
}


function showShotAway() {
    shotAwayButton.classList.remove('hidden');
}

function showMissedAway() {
    missedAway.classList.remove('hidden');
}

function defensiveRebound() {
    missedAway.classList.add('hidden');
    defensiveReb.classList.remove('hidden');
}

function showBlockedShot() {
    missedAway.classList.add('hidden');
    blockedShot.classList.remove('hidden');
}

function block(player) {
    playerBlock = player;
    blockedShot.classList.add('hidden'); // Felur takkan
    actionPost(playerBlock, "", false, "", "", "", "", "", "Block");

}

function showFoulAway() {
    foulAway.classList.remove('hidden');
}

function awayFoul(val) {
    foulAway.classList.add('hidden');
    if (val == 1) {
        foulAwayPlayer.classList.remove('hidden');
    }
}

function FTShotAway(player) {
    playerID = player;
    foulAwayPlayer.classList.add('hidden');
    howManyFTAway.classList.remove('hidden');
}

function addScore(pos, home) { // pos == Three, pos == FT
    if(home) {
        if (pos.includes('Three')) {
            scoreHome += 3;
            empty(homeScoreDiv);
            homeScoreDiv.appendChild(document.createTextNode(scoreHome));
            //Bæta við 3 stigum
        } else if (pos != 'FT') {
            scoreHome += 2;
            empty(homeScoreDiv);
            homeScoreDiv.appendChild(document.createTextNode(scoreHome));
            //Bæta við 2 stigum
        } else {
            scoreHome += 1;
            empty(homeScoreDiv);
            homeScoreDiv.appendChild(document.createTextNode(scoreHome));
            //Bæta við 1 stigi
        }
    } else {
        if (pos.includes('Three')) {
            scoreAway += 3;
            empty(awayScoreDiv);
            awayScoreDiv.appendChild(document.createTextNode(scoreAway));
            shotAwayButton.classList.add('hidden');
            //Bæta við 3 stigum
        } else if (pos != 'FT') {
            scoreAway += 2;
            empty(awayScoreDiv);
            awayScoreDiv.appendChild(document.createTextNode(scoreAway));
            shotAwayButton.classList.add('hidden');
            //Bæta við 2 stigum
        } else {
            scoreAway += 1;
            empty(awayScoreDiv);
            awayScoreDiv.appendChild(document.createTextNode(scoreAway));
            shotAwayButton.classList.add('hidden');
            //Bæta við 1 stigi
        }
    }
}

function showFoulButton() {
    if(playerSelected) { //
        foulButton.classList.remove('hidden');
    }


}

function foul(val) { // val == 1 defensive foul, val = 0 offensive foul
    if (val == 1) { // Shooting foul og non shooting foul
        foulButton.classList.add('hidden');
        foulDefence.classList.remove('hidden');

    } else {
        actionPost(playerId, "", false, "", "", "", "", "Turnover", "Foul");
        foulButton.classList.add('hidden');

    }
}

function defensiveFoul(val) { // val == 1 shooting foul, val == 0 non shooting foul
    if (val == 1) {
        foulDefence.classList.add('hidden');
        howManyFT.classList.remove('hidden');
    } else {
        foulDefence.classList.add('hidden');
    }
    actionPost(playerId, "", false, "", "", "", "", "", "Foul");
}

function freeThrowType(val, home) {
    FTshots = val;
    if (home) {
        howManyFTAway.classList.add('hidden');
        freeThrowAway.classList.remove('hidden');
        if (val == 4) { // Eitt skot
            FTshots = 1;
            addScore('TwoPoint', true);
        }
    } else {
        howManyFT.classList.add('hidden');
        freeThrowShot.classList.remove('hidden');
        if (val == 4) { // Eitt skot
            FTshots = 1;
            addScore('TwoPoint', false);
        }
    }
}

function freeThrow(val, home) {
    if (home) {
        if (FTshots > 0) {
            if (val == 1) { // Made FT
                addScore('FT', true);
                FTshots -= 1;
                actionPost(playerId,"FreeThrow",true,"","","","","","");

            } else if (val == 0) { // TODO bæta við i database fyrir home liðið playerID
                FTshots -= 1;
                actionPost(playerId,"FreeThrow",false,"","","","","","");
            }
        }
        if (FTshots == 0) {
            freeThrowAway.classList.add('hidden');
        }
        return;
    } else {
        if (FTshots > 0) {
            if (val == 1) { // Made FT
                addScore('FT', false);
                FTshots -= 1;
            } else if (val == 0) {
                FTshots -= 1;
            }
        }
        if (FTshots == 0) {
            freeThrowShot.classList.add('hidden');
        }
        return;
    }
}

function showTurnoverPlayer() {
    turnoverPlayer.classList.remove('hidden');
}



function showTurnoverButton() {
    if(playerSelected) { //
        turnoverButt.classList.remove('hidden');
    }
}

function steal(player) {
    turnoverPlayer.classList.add('hidden');
    if (player) {
        playerSteal = player;
        console.log(playerSteal);
        actionPost(playerSteal, "", false, "", "", "", "", "", "Steal");
    } else { //TODO finna út hvernig er hægt að sjá hvaða player er selected
       // playerSteal = playerSelected;

    }



}

function turnover(val) {
    turnoverButt.classList.add('hidden');
    actionPost(playerId, "", false, "", "", "", "", "Turnover", "");
}

function assist(player) {
    if (player != 0) { // TODO viljum ekki hafa assist ef það er valið 'none'
        playerAssist = player;
    }
    assistButton.classList.add('hidden'); // Felur takkan
    playerShoting.classList.remove('hidden');
    console.log('Assist by ' + player);
    //playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other
    actionPost(playerId, shotFrom, true, playerAssist, "", "", "", "", "");
}

function rebound(player) {
    if (player != 0) { // Ef 0 þá á ekki að setja í database
        playerRebound = player;
    }
    reboundButton.classList.add('hidden'); // Felur takkan
    defensiveReb.classList.add('hidden');
    console.log('Rebound for ' + player);
    actionPost(playerId, shotFrom, false, "", playerRebound, "", "", "", "");
}

function shotPos(pos) {
    if(playerSelected) {
        shotButton.classList.remove('hidden'); // Birtir takkan

    }

    shotFrom = pos;
}

async function selectedPlayer(val){
        playerSelected = true;
        playerId = val;
}

 async function actionPost(playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other){

    const action = {
        "playerId": playerId,
        "from": from,
        "isHit": isHit,
        "assist": assist,
        "rebound": rebound,
        "subIn": subIn,
        "subOut": subOut,
        "turnover": turnover,
        "other": other,

    };



    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "/game",
        data : JSON.stringify(action),
        dataType : 'json',
        timeout : 100000,
        success : function(action) {
            console.log("SUCCESS: ", action);
            display(action);
            alert(response);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        done : function(e) {
            console.log("DONE");
        }
    });
}



document.addEventListener("DOMContentLoaded", function() {
    selectedButton = document.getElementsByClassName('funktradio-info');
    shotButton = document.querySelector('.shot');
    shotAwayButton = document.querySelector('.shotAway');
    assistButton = document.querySelector('.assist');
    reboundButton = document.querySelector('.rebound');
    homeScoreDiv = document.querySelector('.home');
    awayScoreDiv = document.querySelector('.away');
    foulButton = document.querySelector('.foul');
    foulDefence = document.querySelector('.foulDefence');
    freeThrowButt = document.querySelector('.freethrows');
    howManyFT = document.querySelector('.howManyFT');
    freeThrowShot = document.querySelector('.freeThrow');
    turnoverButt = document.querySelector('.turnover');
    defensiveReb = document.querySelector('.defensiveRebound');
    missedAway = document.querySelector('.missedAway');
    blockedShot = document.querySelector('.blockedShot');
    foulAway = document.querySelector('.foulAway');
    foulAwayPlayer = document.querySelector('.foulAwayPlayer');
    howManyFTAway = document.querySelector('.howManyFTAway');
    freeThrowAway = document.querySelector('.freeThrowAway');
    turnoverPlayer = document.querySelector('.turnoverPlayer');
});
