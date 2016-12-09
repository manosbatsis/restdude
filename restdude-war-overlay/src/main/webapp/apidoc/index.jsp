<!DOCTYPE html>
<%@ include file="../includes/config.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${appName}: Swagger UI</title>
    <link rel="icon" type="image/png" href="webjars/springfox-swagger-ui/images/favicon-32x32.png" sizes="32x32"/>
    <link rel="icon" type="image/png" href="webjars/springfox-swagger-ui/images/favicon-16x16.png" sizes="16x16"/>
    <link href='${basePath}/webjars/springfox-swagger-ui/css/typography.css' media='screen' rel='stylesheet'
          type='text/css'/>
    <link href='${basePath}/webjars/springfox-swagger-ui/css/reset.css' media='screen' rel='stylesheet'
          type='text/css'/>
    <link href='${basePath}/webjars/springfox-swagger-ui/css/screen.css' media='screen' rel='stylesheet'
          type='text/css'/>
    <link href='${basePath}/webjars/springfox-swagger-ui/css/reset.css' media='print' rel='stylesheet' type='text/css'/>
    <link href='${basePath}/webjars/springfox-swagger-ui/css/print.css' media='print' rel='stylesheet' type='text/css'/>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/underscore-min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/backbone-min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/swagger-ui.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/jsoneditor.min.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/marked.js' type='text/javascript'></script>
    <script src='${basePath}/webjars/springfox-swagger-ui/lib/swagger-oauth.js' type='text/javascript'></script>

    <script type='text/javascript'>
        $(function () {
            var springfox = {
                "baseUrl": function () {
                    //var urlMatches = /(.*)\/swagger-ui.html.*/.exec(window.location.href);
                    return "${basePath}"//urlMatches[1];
                },
                "securityConfig": function (cb) {
                    $.getJSON(this.baseUrl() + "/swagger-resources/configuration/security", function (data) {
                        cb(data);
                    });
                },
                "uiConfig": function (cb) {
                    $.getJSON(this.baseUrl() + "/swagger-resources/configuration/ui", function (data) {
                        cb(data);
                    });
                }
            };
            window.springfox = springfox;
            window.oAuthRedirectUrl = springfox.baseUrl() + '/webjars/springfox-swagger-ui/o2c.html';

            window.springfox.uiConfig(function (data) {
                window.swaggerUi = new SwaggerUi({
                    dom_id: "swagger-ui-container",
                    validatorUrl: data.validatorUrl,
                    supportedSubmitMethods: data.supportedSubmitMethods || ['get', 'post', 'put', 'delete'/*, 'patch'*/],
                    onComplete: function (swaggerApi, swaggerUi) {

                        initializeSpringfox();

                        if (window.SwaggerTranslator) {
                            window.SwaggerTranslator.translate();
                        }

                        $('pre code').each(function (i, e) {
                            hljs.highlightBlock(e)
                        });

                    },
                    onFailure: function (data) {
                        log("Unable to Load SwaggerUI");
                    },
                    docExpansion: data.docExpansion || 'none',
                    jsonEditor: data.jsonEditor || false,
                    apisSorter: data.apisSorter || 'alpha',
                    defaultModelRendering: data.defaultModelRendering || 'schema',
                    showRequestHeaders: data.showRequestHeaders || true
                });

                initializeBaseUrl();

                function addApiKeyAuthorization() {
                    var key = (window.apiKeyVehicle == 'query') ? encodeURIComponent($('#input_apiKey')[0].value) : $('#input_apiKey')[0].value;
                    if (key && key.trim() != "") {
                        var apiKeyAuth = new SwaggerClient.ApiKeyAuthorization(window.apiKeyName, key, window.apiKeyVehicle);
                        window.swaggerUi.api.clientAuthorizations.add(window.apiKeyName, apiKeyAuth);
                        log("added key " + key);
                    }
                }

                $('#input_apiKey').change(addApiKeyAuthorization);

                function log() {
                    if ('console' in window) {
                        console.log.apply(console, arguments);
                    }
                }

                function oAuthIsDefined(security) {
                    return security.clientId
                            && security.clientSecret
                            && security.appName
                            && security.realm;
                }

                function initializeSpringfox() {
                    var security = {};
                    window.springfox.securityConfig(function (data) {
                        security = data;
                        window.apiKeyVehicle = security.apiKeyVehicle || 'query';
                        window.apiKeyName = security.apiKeyName || 'api_key';
                        if (security.apiKey) {
                            $('#input_apiKey').val(security.apiKey);
                            addApiKeyAuthorization();
                        }
                        if (typeof initOAuth == "function" && oAuthIsDefined(security)) {
                            initOAuth(security);
                        }
                    });
                }
            });

            $('#select_baseUrl').change(function () {
                window.swaggerUi.headerView.trigger('update-swagger-ui', {
                    url: $('#select_baseUrl').val()
                });
            });

            function maybePrefix(location, withRelativePath) {
                var pat = /^https?:\/\//i;
                if (pat.test(location)) {
                    return location;
                }
                return withRelativePath + location;
            }

            function initializeBaseUrl() {
                var relativeLocation = springfox.baseUrl();

                $('#input_baseUrl').hide();

                $.getJSON(relativeLocation + "/swagger-resources", function (data) {

                    var $urlDropdown = $('#select_baseUrl');
                    $urlDropdown.empty();
                    $.each(data, function (i, resource) {
                        var option = $('<option></option>')
                                .attr("value", maybePrefix(resource.location, relativeLocation))
                                .text(resource.name + " (" + resource.location + ")");
                        $urlDropdown.append(option);
                    });
                    $urlDropdown.change();
                });

            }

        });

    </script>
</head>

<body class="swagger-section">
<div id='header'>
    <div class="swagger-ui-wrap">
        <a id="logo" href="${basePath}">${appName}</a>

        <form id='api_selector'>
            <div class='input'>
                <select id="select_baseUrl" name="select_baseUrl"/>
            </div>
            <div class='input'><input placeholder="http://example.com/api" id="input_baseUrl" name="baseUrl"
                                      type="text"/>
            </div>
            <div class='input'><input placeholder="api_key" id="input_apiKey" name="apiKey" type="text"/></div>
            <div class='input'><a id="explore" href="#" data-sw-translate>Explore</a></div>
        </form>
    </div>
</div>

<div id="message-bar" class="swagger-ui-wrap" data-sw-translate>&nbsp;</div>
<div id="swagger-ui-container" class="swagger-ui-wrap"></div>
</body>
</html>