/* Define API endpoints once globally */
$(document).ready(function() {
    $.fn.api.settings.api = {
        'add location'  : '/api/add/location',
        'add room'      : '/api/add/room',
        'add cabinet'   : '/api/add/cabinet',
        'add chemical'  : '/api/add/chemical',
        'search'        : '/api/search/chemicals?q={query}',
        'compare'       : location.href.split('?')[0] + '/compare/{oldVersion}/with/{newVersion}'
    };

    $('.version.dropdown')
        .dropdown({
            on: 'click',
            action: function(version) {
                location.href = window.location.href.split('?')[0] + '?v=' + version ;
            }
        })
    ;

    $('.firstVersion.dropdown')
        .dropdown()
    ;

    $('.secondVersion.dropdown')
        .dropdown()
    ;

    $('.viewChemical').click(function() {
        location.href = window.location.href.split('?')[0] + '/' + $(this).attr('data') ;
    });

    /* Following Menu Bar */
    $('body')
        .visibility({
            offset         : -10,
            observeChanges : false,
            once           : false,
            continuous     : false,
            onTopPassed: function() {
                requestAnimationFrame(function() {
                    $('.following.bar')
                        .addClass('light fixed')
                        .find('.menu')
                        .removeClass('inverted')
                    ;
                    $('.following .additional.item')
                        .transition('scale in', 750)
                    ;
                });
            },
            onTopPassedReverse: function() {
                requestAnimationFrame(function() {
                    $('.following.bar')
                        .removeClass('light fixed')
                        .find('.menu')
                        .addClass('inverted')
                        .find('.additional.item')
                        .transition('hide')
                    ;
                });
            }
        })
    ;


    $menu = $('#sidebar');
    $menu.sidebar('attach events', '.view-ui');
    $menu.sidebar({
        dimPage          : true,
        transition       : 'overlay',
        mobileTransition : 'uncover'
    });


    $('.addModal').click(function() {
        $('.ui.small.modal')
            .modal({
                closable  : false
            })
            .modal('show');
    });

    $('#newVersion').click(function() {
        $('.ui.small.modal.fork')
            .modal('show');
    });

    $('#compareVersions').click(function() {
        $('.ui.small.modal.compare')
            .modal('show');
    });

    $('#compareVersionForm')
        .form({
            on: 'change',
            fields : {
                firstVersion : {
                    identifier : 'firstVersion',
                    rules: [
                        {
                            type    : 'empty',
                            prompt  : 'Please select a new version to compare.'
                        }
                    ]
                },
                secondVersion : {
                    identifier : 'secondVersion',
                    rules : [
                        {
                            type    : 'different[firstVersion]',
                            prompt  : 'Please select two different versions.'
                        },
                        {
                            type    : 'empty',
                            prompt  : 'Please select an old version to compare.'

                        }
                    ]
                }
            }
        }).api({
            action : 'compare',
            beforeSend : function(settings) {
                settings.urlData = {
                    oldVersion : $('#firstVersion').val(),
                    newVersion : $('#secondVersion').val()
                };
                return settings;
            },
            onSuccess : function(response) {

                var added = '';
                response.added.forEach(function(elem) {
                    added += '<li>' + elem +  '</li>';
                });

                var removed = '';
                response.removed.forEach(function(elem) {
                    removed += '<li>' + elem + '</li>';
                });

                var matching = '';
                response.matching.forEach(function(elem) {
                    matching += '<li>' + elem + '</li>';
                });

                $('#compare-added').html(added);
                $('#compare-removed').html(removed);
                $('#compare-matching').html(matching);
                $('#compare-header').html('Comparison [' + response.oldVersion +
                    ' <i class="caret right icon"></i>' + response.newVersion + ']');
                $('.ui.small.modal.compareResult')
                    .modal('show');
            }
        })
    ;

    $('.ui.search')
        .search({
            minCharacters : 3,
            action: 'search',
            fields: {
                results     : 'chemicals',
                title       : 'name',
                description : ''
            },
            onSelect: function(chemical) {

                $.ajax({
                    type        : 'post',
                    dataType    : 'json',
                    contentType : 'application/json',
                    mimeType    : 'application/json',
                    url         : location.origin + '/api/update/chemical',
                    data        : JSON.stringify({
                        request         : 'ADD',
                        location        : $('#location-name').attr('value'),
                        room            : $('#room-name').attr('value'),
                        cabinet         : $('#cabinet-name').attr('value'),
                        auditVersion    : $('#audit-version').attr('value'),
                        chemical        : chemical.name
                    }),
                    beforeSend: function() {
                        $(".fixedLoader").addClass('active');
                    },
                    success: function(response) {
                        if (response.success) {
                            // TODO: simplify
                            var card = $('#chemicalCardTemplate').clone();
                            card.attr('id', '')
                                .appendTo('#chemicalCards')
                                .find(".header").html(chemical.name);

                            card.find(".image").click(function() {
                                location.href = window.location.href.split('?')[0] + '/' + chemical.name;
                                //window.location+='/' + chemical.name
                            });

                            card.find(".imageURL").attr("src", chemical.imageURL);
                            card.find(".remove").attr("data", name);

                            card.find(".remove").click(function() {

                                var parentCard = $(this).parents().eq(2);

                                $('.ui.basic.modal.confirm')
                                    .modal({
                                        onApprove: function() {
                                            parentCard.remove();
                                            $.ajax({
                                                url: '/api/update/chemical',
                                                type: 'post',
                                                dataType: "json",
                                                contentType: "application/json",
                                                mimeType: "application/json",
                                                data: JSON.stringify({
                                                    request         : 'REMOVE',
                                                    location        : $('#location-name').attr('value'),
                                                    room            : $('#room-name').attr('value'),
                                                    cabinet         : $('#cabinet-name').attr('value'),
                                                    auditVersion    : $('#audit-version').attr('value'),
                                                    chemical        : chemical.name
                                                }),
                                                success: function(response) { },
                                                error: function(e) { }
                                            });
                                        }
                                    })
                                    .modal('show')
                                ;

                            });

                            card.show();
                            $('#noStorages').hide();
                        } else {
                            // Display message to the user
                        }
                    },
                    error: function(e) {
                        console.log("ERROR:");
                        console.log(e);
                    },
                    complete: function(e) {
                        $('.fixedLoader').removeClass('active');
                        $('#no-chemicals-msg').remove();
                    }
                });
            }
        })
    ;

    $('#forkVersionForm').find('.submit').click(function() {

        var forkVersion = $('#forkVersion').val();

        $.ajax({
            type        : 'post',
            dataType    : 'json',
            contentType : 'application/json',
            mimeType    : 'application/json',
            url         : location.origin + '/api/update/cabinet',
            data        : JSON.stringify({
                request         : 'FORK',
                location        : $('#location-name').attr('value'),
                room            : $('#room-name').attr('value'),
                cabinet         : $('#cabinet-name').attr('value'),
                auditVersion    : $('#audit-version').attr('value'),
                forkVersion     : forkVersion,
            }),
            beforeSend: function() {
                $(".fixedLoader").addClass('active');
            },
            success: function(response) {
                console.log('SUCCESSFULLY ADDED');
                // Set the name of the new version
                $('#audit-version').attr('value')
            },
            complete: function(e) {
                $('.fixedLoader').removeClass('active');
            }
        });
    });

    $('#addStorageForm').find('.submit').click(function() {
        var url = $('#add-url').attr('value');
        var formData = new FormData($('#addStorageForm')[0]);

        $.ajax({
            type: 'post',
            url: url,
            contentType: false,
            processData: false,
            cache: false,
            data: formData,
            beforeSend: function() {
                $('.fixedLoader').addClass('active');
            },
            success: function(storage) {
                $('#formSubmissionMsg').find('.header').text("Submission Successful");
                $('#formSubmissionMsg').find('p').text("The object was successfully created!");
                $('#formSubmissionMsg').removeClass('error');
                $('#formSubmissionMsg').addClass("success");

                var card = $('#storageCardTemplate').clone();
                card.attr('id', '')
                    .appendTo('#storageCards')
                    .find(".header").html(storage.name);

                card.find(".image").click(function() {
                    location.href = window.location.href.split('?')[0] + '/' + storage.name;
                    //window.location+='/' + storage.name
                });

                card.find('.description')
                    .html(storage.description);

                card.find(".imageURL").attr("src", storage.imageURL);
                card.find(".remove").attr("data", storage.id);

                card.find(".remove").click(function() {
                    var parentCard = $(this).parents().eq(4);
                    var storageName = $(this).attr('data');
                    var parentID = $('#parentID').attr('value');
                    var url = $('#remove-url').attr('value');
                    $('.ui.basic.modal.confirm')
                        .modal({
                            onApprove: function() {
                                $.ajax({
                                    url: url + storageName + '/from/' + parentID,
                                    type: 'get',
                                    success: function() {
                                        parentCard.remove();
                                    },
                                    error: function(e) { }
                                });
                            }
                        })
                        .modal('show');
                });

                $('.dropdown')
                    .dropdown()
                ;

                card.show();

                $('#noStorages').hide();
            },
            error: function() {
                $('#formSubmissionMsg .header').text("Submission Failed");
                $('#formSubmissionMsg p').text("An error occurred while creating object!");
                $('#formSubmissionMsg').addClass("error");
                $('#formSubmissionMsg').removeClass("success");
            },
            complete: function() {
                $("#noStorages").hide();
                $('#formSubmissionMsg').show();
                $('.fixedLoader').removeClass('active');
            }
        });
    });

    $('.storage .remove')
        .click(function() {
            var parentCard = $(this).parents().eq(4);
            var url = $('#remove-url').attr('value');
            var storageName = $(this).attr('data');
            var parentID = $('#parentID').attr('value');
            $('.ui.basic.modal.confirm')
                .modal({
                    onApprove: function() {
                        $.ajax({
                            url: url + storageName + '/from/' + parentID,
                            type: 'get',
                            success: function() {
                                parentCard.remove();
                            },
                            error: function(e) { }
                        });
                    }
                })
                .modal('show');
        })
    ;

    $('.chemical .remove')
        .click(function() {
            var parentCard = $(this).parents().eq(2);
            var chemicalName = $(this).attr('data');
            $('.ui.basic.modal.confirm')
                .modal({
                    onApprove: function() {
                        parentCard.remove();
                        $.ajax({
                            url: '/api/update/chemical',
                            type: 'post',
                            dataType: "json",
                            contentType: "application/json",
                            mimeType: "application/json",
                            data: JSON.stringify({
                                request         : 'REMOVE',
                                location        : $('#location-name').attr('value'),
                                room            : $('#room-name').attr('value'),
                                cabinet         : $('#cabinet-name').attr('value'),
                                auditVersion    : $('#audit-version').attr('value'),
                                chemical        : chemicalName
                            }),
                            success: function() {
                                $('#noStorages').hide();
                            },
                            error: function(e) { }
                        });
                    }
                })
                .modal('show');
        })
    ;

    $('.button').popup({
        variation: 'inverted',
        position: 'top center'
    });


    $('.message .close').on('click', function() {
        $(this).closest('.message')
            .transition('fade');
    });
});