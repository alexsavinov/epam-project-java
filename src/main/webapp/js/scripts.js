function sortClick(id) {
    const elem = document.getElementById(id);
    elem.value = (elem.value === "asc") ? "desc" : "asc";
    console.log(elem.value)
}

function setValue(id, value) {
    document.getElementById(id).value = value;
}

function setAttributeValue(id, attribute, value) {
    document.getElementById(id).setAttribute(attribute, value);
}

function setEmpty(id) {

    const element = document.getElementById(id);
    element.value = '';
    element.classList.remove("border", "border-danger", "border-2");

}

function isValidValue(element, res, message) {
    if (res) {
        // $('#submit_button').addClass("disabled"); // TODO uncomment in prod!
        $(element).attr('data-bs-title', message)
        element.classList.add('border', 'border-danger', 'border-2');
        new bootstrap.Tooltip(element);
        $(element).tooltip('show');
        return false;
    } else {
        $('#submit_button').removeClass("disabled");
        element.classList.remove("border", "border-danger", "border-2");
        $(element).click();
        return true;
    }
}

function validate(element) {

    $('.tooltip').remove();

    console.log('------------- element.id', element.id);
    switch (element.id) {
        case 'cost_min':
            const cost_max = parseInt(document.getElementById('cost_max').value);
            if (!isValidValue(element,
                cost_max > 0 && element.value > cost_max,
                'Enter value less than ' + cost_max)) {
                return;
            }
            break;
        case "cost_max":
            const cost_min = parseInt(document.getElementById('cost_min').value);
            if (!isValidValue(element,
                cost_min > 0 && element.value < cost_min,
                'Enter value more than ' + cost_min)) {
                return;
            }
            break;
        case "travel_time_min":
            const travel_time_max = parseInt(document.getElementById("travel_time_max").value);
            if (travel_time_max) element.value = Math.min(travel_time_max, element.value);
            if (!isValidValue(element,
                travel_time_max > 0 && element.value > travel_time_max,
                'Enter value less than ' + travel_time_max)) {
                return;
            }
            break;
        case "travel_time_max":
            const travel_time_min = parseInt(document.getElementById("travel_time_min").value);
            if (!isValidValue(element,
                travel_time_min > 0 && element.value < travel_time_min,
                'Enter value more than ' + travel_time_min)) {
                return;
            }
            break;
        case "seats_available_min":
            const seats_available_max = parseInt(document.getElementById("seats_available_max").value);
            if (seats_available_max) element.value = Math.min(seats_available_max, element.value);
            if (!isValidValue(element,
                seats_available_max > 0 && element.value > seats_available_max,
                'Enter value less than ' + seats_available_max)) {
                return;
            }
            break;
        case "seats_available_max":
            const seats_available_min = parseInt(document.getElementById("seats_available_min").value);
            if (!isValidValue(element,
                seats_available_min > 0 && element.value < seats_available_min,
                'Enter value more than ' + seats_available_min)) {
                return;
            }
            break;
        case "password":
            if (element.value) {
                const passwordRGEX = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])([a-zA-Z0-9]{8})$/;
                const passwordResult = passwordRGEX.test(element.value || "");
                $('#submit_button').removeClass("disabled");
                if (!isValidValue(element, !passwordResult, 'Password is incorrect!')) {
                    return;
                }
            }
            break;
        case "email":
            if (element.value) {
                const emailRGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                const emailResult = emailRGEX.test(element.value || "");
                if (!isValidValue(element, !emailResult, 'Email is incorrect!')) {
                    $('#register_submit_button').addClass("disabled");
                    return;
                } else {
                    $('#register_submit_button').removeClass("disabled");
                }
            }
            break;
        case "password2":
            if (element.value) {
                const password = document.getElementById("password");
                console.log('password.value', password.value);
                console.log('element.value', element.value);
                if (password.value == element.value) {
                    $('#register_submit_button').removeClass("disabled");
                } else {
                    $('#register_submit_button').addClass("disabled");
                    isValidValue(element, true, 'Password mismatch!');
                }
            }
            break;
        case "name":
            // console.log("name", element.value)
            if (element.value) {
                const nameRGEX = /^[a-zA-Z0-9_]{4,100}[a-zA-Z]+[0-9]*$/;
                const nameResult = nameRGEX.test(element.value || "");
                $('#submit_button').removeClass("disabled");
                if (!isValidValue(element, !nameResult, 'Name is incorrect!')) {
                    return;
                }
            }
            break;
        default:
    }

    const min = parseInt(element.getAttribute("min"));
    if (min != null) {
        if (!isValidValue(element,
            element.value < min,
            'Minimum value is ' + min)) {
            return;
        }
    }

    const max = parseInt(element.getAttribute("max"));
    if (max != null) {
        if (!isValidValue(element,
            element.value > max,
            'Maximum value is ' + max)) {
            return;
        }
    }

}

function showPopover(element) {
    $(element).popover('show');
}

// DATE RANGE settings
const startDate = new Date();
const endDate = new Date();
startDate.setMonth(endDate.getMonth() - 2);
endDate.setMonth(endDate.getMonth() + 2);
$(function () {
    $('input[name="daterange"]').daterangepicker({
        startDate: startDate,
        endDate: endDate,
        // timePicker: true,
        // timePickerIncrement: 30,
        locale: {
            format: 'DD/MM/YYYY'
        }
    });
});

function goBack(url) {
    console.log('url=', url)
    if (url) {
        window.location.href = url;
    } else {
        history.back();
    }
}

function goByUrl(a) {
    window.location.href = a;
}

function updateTime() {
    const date_departure = document.getElementById("date_departure").value;
    const date_arrival = document.getElementById("date_arrival").value;
    const date1 = new Date(date_departure);
    // const date1 = moment(new Date(date_departure)).format('YYYY-MM-DDTHH:mm');
    const date2 = new Date(date_arrival);
    const diffTime = Math.abs(date2 - date1);

    document.getElementById("travel_time").value = msToTime(diffTime);
}

function msToTime(duration) {
    let milliseconds = Math.floor((duration % 1000) / 100),
        seconds = Math.floor((duration / 1000) % 60),
        minutes = Math.floor((duration / (1000 * 60)) % 60),
        hours = Math.floor((duration / (1000 * 60 * 60)) % 24),
        days = Math.floor(duration / (1000 * 60 * 60 * 24));

    if (seconds) seconds = (seconds == 1) ? "1 second" : seconds + " seconds";
    if (minutes) minutes = (minutes == 1) ? "1 minute " : minutes + " minutes ";
    if (hours) hours = (hours == 1) ? "1 hour " : hours + " hours ";
    if (days) days = (days == 1) ? "1 day " : days + " days ";

    return ((days) ? days : "")
        + ((hours) ? hours : "")
        + ((minutes) ? minutes : "")
        + ((seconds) ? seconds : "");
}

function updateUser() {
    const id = document.getElementById("user").value;
    $.ajax({
        url: "/users/get/" + id,
        type: "POST",
        // data: new FormData(document.getElementById("fileForm")),
        data: {
            user_id: id,
            json: true
        },
        // enctype: 'multipart/form-data',
        // processData: false,
        // contentType: false,
        success: function (res) {
            console.log('RESPONSE: ', res);
            // $(".impostSuccess").modal('show');
            // file.val('');
            document.getElementById("user_id").value = res.id;
            document.getElementById("user_name").value = res.name;
        },
        error: function (err) {
            // file.val('');
            console.log('RESPONSE: ', err);
            // $(".impostFail").modal('show');
        }
    });
}

function updateRoute() {
    const id = document.getElementById("route").value;

    $.ajax({
        url: "/routes/getById/" + id,
        type: "POST",
        data: {
            reqValue: id
        },
        success: function (res) {
            // console.log('RESPONSE: ', res);
            document.getElementById("train_number").value = res.train_number;
            document.getElementById("station_departure").value = res.station_departure.name;
            document.getElementById("station_arrival").value = res.station_arrival.name;
            document.getElementById("date_departure").value = res.date_departure;
            document.getElementById("date_arrival").value = res.date_arrival;
            document.getElementById("travel_cost").value = res.travel_cost;
            document.getElementById("seats_available").value = res.seats_available;
            document.getElementById("seats_total").value = res.seats_total;
        },
        error: function (err) {
            console.log('ERROR: ', err);
        }
    });

    updateTime();
}

function updateRoutes() {

    $.ajax({
        url: "/routes/getAll",
        type: "POST",
        // data: {
        //     reqValue: id
        // },
        success: function (res) {
            // console.log('RESPONSE: ', res);
            document.getElementById("train_number").value = res.train_number;
            document.getElementById("station_departure").value = res.station_departure.name;
            document.getElementById("station_arrival").value = res.station_arrival.name;
            document.getElementById("date_departure").value = res.date_departure;
            document.getElementById("date_arrival").value = res.date_arrival;
            document.getElementById("travel_cost").value = res.travel_cost;
            document.getElementById("seats_available").value = res.seats_available;
            document.getElementById("seats_total").value = res.seats_total;
        },
        error: function (err) {
            console.log('ERROR: ', err);
        }
    });

    updateTime();
}

function resetSearchForm() {
    $.ajax({
        url: "/search",
        type: "POST",
        data: {
            action: "reset",
        },
        success: function () {
            location.reload();
        }
    });
}

function submitRegistrationForm() {
    const frm = $('#registerForm');
    frm.submit(function (e) {
        if ($('#password').value !== $('#password2').value) {
            e.preventDefault();
        }
    });
}

function submitForm() {
    const frm = $('#searchForm');

    frm.submit(function (e) {

        e.preventDefault();

        $.ajax({
            type: frm.attr('method'),
            url: frm.attr('action'),
            data: {
                'action': 'search',
                'from': frm.serialize()
            },
            success: function (res) {
                console.log('Submission was successful.');
                console.log(res);

                // clear child nodes
                const routes = document.getElementById("routes");
                while (routes.firstChild) {
                    routes.removeChild(routes.lastChild);
                }

                const divWrapper = document.createElement("div");
                divWrapper.classList.add('_wrapper-table');
                routes.appendChild(divWrapper);

                // creating table
                const table = document.createElement('table');
                table.classList.add('table', 'table-striped', 'table-hover', 'p-5', 'mt-3');
                divWrapper.appendChild(table);

                const thead = document.createElement('thead');
                table.appendChild(thead);

                const trHead = document.createElement('tr');
                thead.appendChild(trHead);

                appendTr(trHead, 'Train #');
                appendTr(trHead, 'Departure');
                appendTr(trHead, 'Date/time departure');
                appendTr(trHead, 'Travel time');
                appendTr(trHead, 'Arrival');
                appendTr(trHead, 'Date/time arrival');
                appendTr(trHead, 'Travel cost');
                appendTr(trHead, 'Seats total');
                appendTr(trHead, 'Seats available');

                const tbody = document.createElement('tbody');
                table.appendChild(tbody);

                let tr, td, th;

                for (let i = 0; i < res.data.length; i++) {
                    tr = document.createElement('tr');
                    tr.setAttribute("onclick", 'changeFunc("/search/reserve/' + i + '")');
                    tr.id = 'row' + i;
                    tbody.appendChild(tr);

                    th = document.createElement('th');
                    th.setAttribute('scope', 'col');
                    th.innerText = res.data[i].train_number;
                    tr.appendChild(th);

                    appendTd(tr, res.data[i].station_departure.name);
                    appendTd(tr, res.data[i].date_departure);
                    appendTd(tr, 0);
                    appendTd(tr, res.data[i].station_arrival.name);
                    appendTd(tr, res.data[i].date_arrival);
                    appendTd(tr, res.data[i].travel_cost);
                    appendTd(tr, res.data[i].seats_total);
                    appendTd(tr, res.data[i].seats_available);
                }

                console.log(routes)
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        });
    });
}

function appendTr(parentElement, tittle) {
    const element = document.createElement('th');
    element.setAttribute('scope', 'col');
    element.innerText = tittle;
    parentElement.appendChild(element);
}

function appendTd(parentElement, tittle) {
    const element = document.createElement('td');
    element.innerText = tittle;
    parentElement.appendChild(element);
}

// pagination
function goByPage(pg_page) {
    if (pg_page) {
        document.getElementById("pg_page").value = pg_page;
        // const sort_train_number = document.getElementById("sort_train_number");
        // sort_train_number.value = (sort_train_number.value === "asc") ? "desc" : "asc";
        // document.getElementById("paginatorForm").submit();
        document.getElementById("searchForm").submit();
    }
}
