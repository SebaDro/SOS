<%--

    Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
    Software GmbH

    This program is free software; you can redistribute it and/or modify it
    under the terms of the GNU General Public License version 2 as published
    by the Free Software Foundation.

    If the program is linked with libraries which are licensed under one of
    the following licenses, the combination of the program with the linked
    library is not considered a "derivative work" of the program:

        - Apache License, version 2.0
        - Apache Software License, version 1.0
        - GNU Lesser General Public License, version 3
        - Mozilla Public License, versions 1.0, 1.1 and 2.0
        - Common Development and Distribution License (CDDL), version 1.0

    Therefore the distribution of the program linked with libraries licensed
    under the aforementioned licenses, is permitted by the copyright holders
    if the distribution is compliant with both the GNU General Public
    License version 2 and the aforementioned licenses.

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
    Public License for more details.

--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="../common/header.jsp">
    <jsp:param name="activeMenu" value="admin" />
</jsp:include>

<link rel="stylesheet" href="<c:url value="/static/lib/prettify.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/static/lib/codemirror-2.34.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/static/css/codemirror.custom.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/static/lib/bootstrap-toggle-buttons.css" />" type="text/css" />
<script type="text/javascript" src="<c:url value="/static/lib/codemirror-2.34.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/codemirror-2.34-xml.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/prettify.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/vkbeautify-0.99.00.beta.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.toggle.buttons.js" />"></script>

<script type="text/javascript" src="<c:url value="/static/js/jquery.additions.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/js/EventMixin.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/js/MqttConfigurationController.js" />"></script>

<jsp:include page="../common/logotitle.jsp">
    <jsp:param name="title" value="MQTT Setting" />
    <jsp:param name="leadParagraph" value="Use this site to define your MQTT settings." />
</jsp:include>

<style type="text/css">
    .btn-icon { height: 30px; }
    .btn-icon i { margin-right: 0px !important; }
    .btn-single { margin-bottom: 1px;}
    #stcaps-publish { margin-bottom: -11px; }
    #back-top {
        position: fixed;
        bottom: 30px;
        margin-left: -150px;
    }
    #back-top a {
        width: 108px;
        display: block;
        text-align: center;
        font: 11px Arial, Helvetica, sans-serif;
        text-transform: uppercase;
        text-decoration: none;
        color: #bbb;
        /* background color transition */
        -webkit-transition: 1s;
        -moz-transition: 1s;
        transition: 1s;
    }
    #back-top a:hover {
        color: #000;
    }
    #back-top i {
        display: block;
        margin-bottom: 7px;
        margin-left: 48px;
        -webkit-border-radius: 15px;
        -moz-border-radius: 15px;
        border-radius: 15px;
        -webkit-transition: 1s;
        -moz-transition: 1s;
        transition: 1s;
    }
    #mqttconf-loader {
      display: none;
      width: 30px;
      height: 30px;
    }
</style>

<div id="mqtt-configuration-container" class="column">
    <div class="row">
        <div class="span12 form-inline" style="margin-bottom: 5px;">
            <select id="mqttconf-list" class="span6">
                <c:forEach var="config" items="${mqttConfigurations}">
                    <option value="${config.key}">${config.name}</option>
                </c:forEach>
            </select>
            <div class="btn-group">
                <!--<button data-target="#confirmDialogClear" data-toggle="modal" title="Clear Datasource" class="btn btn-danger">Clear Datasource</button>-->
                <button data-target="#addMqttConfigDialog" data-toggle="modal" id="mqttconf-addnew-button" title="Add new MQTT configuration" type="button" class="btn btn-icon mqttconf-edit-button"><i class="icon-plus"></i></button>
                <!--<button id="mqttconf-addnew-button" title="Add new MQTT configuration" type="button" class="btn btn-icon mqttconf-edit-button"><i class="icon-plus"></i></button>-->
            </div>
            <div class="pull-right">
              <img id="mqttconf-loader" src="/static/images/loader.gif">
              <button id="mqttconf-activate-button" title="Activate" type="button" class="btn btn-danger pull-right" style="margin-left: 10px">active</button>
            </div>
            <div id="mqttconf-add-new-form" class="input-append input-prepend control-group" style="display: none;">
                <input class="input-xlarge" id="mqttconf-new-name-input" type="text" placeholder="Identifier"/>
                <div class="btn-group">
                    <button type="button" title="Add" class="btn btn-icon mqttconf-add-new-form-button" id="mqttconf-add-new-form-ok"><i class="icon-ok"></i></button>
                    <button type="button" title="Dismiss" class="btn btn-icon mqttconf-add-new-form-button" id="mqttconf-add-new-form-cancel"><i class="icon-remove"></i></button>
                </div>
            </div>
        </div>
    </div>

    <div id="mqtt-configuration-properties-container" class="column">
        <legend >
            <div class="row">
                <div>Configuration for MQTT client </div>
            </div>
        </legend>
        <div id="mqtt-configuration-controls" class="column">
        <div id="mqttconf-name-input-control-group" class="control-group">
            <label class="control-label" for="mqttconf-name-input">MQTT configuration name</label>
            <div class="controls">
                <input type="text" class="input-xlarge" id="mqttconf-name-input" value="${selectedMqttConfiguration.name}" required>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-host-input">MQTT broker host</label>
            <div class="controls">
                <input type="text" class="input-xlarge" id="mqttconf-host-input" value="${host}" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-port-input">MQTT broker port</label>
            <div class="controls">
                <input type="text" class="input-xlarge"  id="mqttconf-port-input" value="${port}" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-topic-input">MMQTT broker topic</label>
            <div class="controls">
                <input type="text" class="input-xlarge" id="mqttconf-topic-input" value="${selectedMqttConfiguration.topic}" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-protocol-select">MQTT protocol</label>
            <div class="controls">
                <select id="mqttconf-protocol-select" class="input-xlarge">
                    <option selected>tcp</option>
                    <option>ws</option>
                </select>
                <span class="help-block">Select the protocol</span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-decoder-select">MQTT decoder</label>
            <div class="controls">
                <select id="mqttconf-decoder-select" class="input-xlarge"></select>
                <span class="help-block">Select the decoder</span>
              </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mqttconf-batch-request-checkbox">
              <input id="mqttconf-batch-request-checkbox" type="checkbox"/>
              Use batch request
            </label>
            <span class="help-block">Choose whether multiple MQTT messages should be inserted by one InsertObservation batch request or not</span>
        </div>
        <div id="mqttconf-batch-limit" class="control-group">
              <label class="control-label" for="mqttconf-batch-limit-input">Number of messages for batch request</label>
              <div class="controls">
                <input type="text" class="input-xlarge" id="mqttconf-batch-limit-input" value="${batchLimit}"/>
              </div>
        </div>
      </div>
        </div>
        <div class="form-actions">
            <button id="mqttconf-save-button" class="btn btn-info">Save</button>
        </div>
    </div>
    <div class="modal hide fade in" id="addMqttConfigDialog">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>Configuration for a new MQTT client</h3>
        </div>
        <div id="addMqttConfigDialog-modal-body" class="modal-body">
            <p></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
            <button type="button" id="mqttconf-createnew-button" class="btn btn-success">Create</button>
        </div>
    </div>
<script type="text/javascript">
    var baseUrl = "<c:url value='/'/>";
    new MqttConfigurationController(baseUrl);
</script>

        <jsp:include page="../common/footer.jsp"/>
