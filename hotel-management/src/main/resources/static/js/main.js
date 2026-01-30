$(document).ready(function() {

    $('main').hide().fadeIn(800);


    $('.confirm-action').on('click', function(e) {
        let message = $(this).data('confirm') || "Sei sicuro di procedere?";
        if (!window.confirm(message)) {
            e.preventDefault(); 
        }
    });


    $('table tr').on('mouseover', function() {
        $(this).css('background-color', '#eef2f3');
    }).on('mouseout', function() {
        $(this).css('background-color', '');
    });


    $('#bookingForm').on('submit', function(e) {
        let start = new Date($('#startDate').val());
        let end = new Date($('#endDate').val());
        if (end <= start) {
            alert("La data di partenza deve essere successiva a quella di arrivo!");
            e.preventDefault();
        }
    });

    $("#tableSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("table tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });


    $('#registrationForm').on('submit', function(e) {
        let pass = $('#password').val();
        let confirmPass = $('#confirmPassword').val();
        if (pass !== confirmPass) {
            alert("Le password inserite non coincidono!");
            e.preventDefault();
        }
    });
});