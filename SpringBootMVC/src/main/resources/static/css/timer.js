var countdownTimer;


var stop = false;

function empty(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}

function setup() {
    var min = prompt("Enter time for the clock");

    if(min!=null){
        this.countdownTimer = minToSec(min);
    }
}

function minToSec(time){
    return time * 60;
}

function startStop(){
    if(!this.stop) {
        this.stop = true;
        sessionStorage.setItem('autosaveStop', this.stop);
    }
    else {
        this.stop = false;
        sessionStorage.setItem('autosaveStop', this.stop);
    }
}

function formatTime(time){
    var minute = Math.floor(time / 60) ;
    var seconds = time % 60 + 0.0 ;

    return minute + ":" + seconds;
}

var time = 0,
    elapsed = '0.0';

document.addEventListener("DOMContentLoaded", function() {
    var timer = document.querySelector('.timer');

    window.setInterval(function()
{
    time += 1000;

    elapsed = Math.floor(time / 100) / 10;
    if(Math.round(elapsed) == elapsed) { elapsed += '.0'; }

    if(countdownTimer > -1 && stop){
        empty(timer);
        timer.appendChild(document.createTextNode(formatTime(countdownTimer)));
        countdownTimer--;
    }

}, 1000);
});
