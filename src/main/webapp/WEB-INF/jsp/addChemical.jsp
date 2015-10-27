<%@include file="templates/header.jsp" %>

<body>
    <%@include file="templates/navbar.jsp" %>
    <div class="wrapper wrapper-fluid">
        <h2>Add New Chemical</h2>
        <form action="/addChemical" method="post">

            <div class="form_group">
                <label>Chemical Name</label>
                <input type="text" name="Name"/>
            </div>

            <div class="form_group">
                <label>First Aid Measures</label>
                <textarea name="FirstAidMeasures"></textarea>
            </div>

            <h5>Fire Diamond</h5>

            <div class="form_group row">
                <label class="col-2">Flammability</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="Flammability"/>
                </div>
            </div>

            <div class="form_group row">
                <label class="col-2">Health</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="Health"/>
                </div>
            </div>

            <div class="form_group row">
                <label class="col-2">Instability</label>
                <div class="col-1">
                    <input type="number" min="0" max="5" name="Instability"/>
                </div>
            </div>

            <div class="form_group">
                <label>Notice</label>
                <textarea name="Notice"></textarea>
            </div>

            <h5>Fire and Explosion Data</h5>

            <div class="form_group">
                <label>Hazard in presence of</label>
                <textarea name="HazardInPresenceOf"></textarea>
            </div>

            <div class="form_group">
                <label>Other Details</label>
                <textarea name="OtherDetails"></textarea>
            </div>

            <h5>Handling and Storage</h5>

            <div class="form_group">
                <label>Precautions</label>
                <textarea name="Precautions"></textarea>
            </div>

            <div class="form_group">
                <label>Storage</label>
                <textarea name="Storage"></textarea>
            </div>

            <div class="form_group">
                <label>Exposure Controls</label>
                <textarea name="ExposureControls"></textarea>
            </div>

            <h5>Physical and Chemical Properties</h5>

            <div class="form_group">
                <label>Boiling Point</label>
                <input type="number" name="BoilingPoint"/>
            </div>

            <div class="form_group">
                <label>Melting Point</label>
                <input type="number" name="MeltingPoint"/>
            </div>

            <div class="form_group">
                <label>Solubility</label>
                <textarea name="Solubility"></textarea>
            </div>

            <br/>

            <div class="form_group row">
                <input class="button button-green button-large col-left-9" type="submit" value="Submit"/>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>

        <br/>
        <br/>
    </div>
</body>

<%@include file="templates/footer.jsp" %>
