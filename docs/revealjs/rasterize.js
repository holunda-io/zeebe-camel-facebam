var page = require('webpage').create()
var system = require('system');


var address = "http://127.0.0.1:9000/index.html?print-pdf=screen&transition=none/#";
var output = "slides";
var outputFormat = ".pdf"
page.viewportSize = { width: 960, height: 700 };
page.paperSize = {
    format: 'A4',
    orientation: 'landscape',
    border: '0cm',
    margin: {
        left: '0',
        right: '0',
        top: '0',
        bottom: '0'
    }
};
page.zoomFactor = 0.75;

var slideCounter = 1

function onSlide(page, slide) {

    page.render(output + (slideCounter++) + outputFormat);


    if (!hasMoreSlides(page)) {
        console.log('Last slide reached! We are done here.');
        phantom.exit();
    }
    else {
        page.evaluate(function () {
            Reveal.next();
        });

        waitFor(function () {
            console.log('Waiting for slide ' + slide);
            // Check if we are on the next slide already
            return getCurrentSlide(page) != slide;
        }, function () {
            onSlide(page, getCurrentSlide(page));
        });
    }
}

function getCurrentSlide(page) {
    var currentSlide = page.evaluate(function () {
        return window.currentSlide;
    });
    console.log('Get current slide: ' + currentSlide);
    return currentSlide;
}

function hasMoreSlides(page) {
    return page.evaluate(function () {
        return !Reveal.isLastSlide();
    });
}

page.open(address, function (status) {
        if (status !== 'success') {
            console.log('Unable to load the address!');
            phantom.exit();
        } else {

            window.setTimeout(function () {
                page.evaluate(function () {
                    Reveal.addEventListener('slidechanged', function (event) {
                        window.currentSlide = event.currentSlide;
                    });
                });
                onSlide(page, getCurrentSlide(page));
            }, 400);
        }
    }
)
;


/**
 * Wait until the test condition is true or a timeout occurs. Useful for waiting
 * on a server response or for a ui change (fadeIn, etc.) to occur.
 *
 * @param testFx javascript condition that evaluates to a boolean,
 * it can be passed in as a string (e.g.: "1 == 1" or "$('#bar').is(':visible')" or
 * as a callback function.
 * @param onReady what to do when testFx condition is fulfilled,
 * it can be passed in as a string (e.g.: "1 == 1" or "$('#bar').is(':visible')" or
 * as a callback function.
 * @param timeOutMillis the max amount of time to wait. If not specified, 3 sec is used.
 */
function waitFor(testFx, onReady, timeOutMillis) {
    var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 3000, //< Default Max Timout is 3s
        start = new Date().getTime(),
        condition = false,
        interval = setInterval(function () {
            if ((new Date().getTime() - start < maxtimeOutMillis) && !condition) {
                // If not time-out yet and condition not yet fulfilled
                condition = (typeof(testFx) === "string" ? eval(testFx) : testFx()); //< defensive code
            } else {
                if (!condition) {
                    // If condition still not fulfilled (timeout but condition is 'false')
                    console.log("'waitFor()' timeout");
                    phantom.exit(1);
                } else {
                    // Condition fulfilled (timeout and/or condition is 'true')
                    console.log("'waitFor()' finished in " + (new Date().getTime() - start) + "ms.");
                    typeof(onReady) === "string" ? eval(onReady) : onReady(); //< Do what it's supposed to do once the condition is fulfilled
                    clearInterval(interval); //< Stop this interval
                }
            }
        }, 100); //< repeat check every 250ms
};