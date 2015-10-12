#Chemical Tracker

A chemical inventory server that allows users to dynamically develop an inventory of chemicals as
well as retrieve information on specific chemicals from any place where Internet is availabled.

Potential use cases include giviing first responders the ability to identify hazardous materials;
allowing laboratory auditors to inventory chemicals that are found in labs; or providing users
with information about chemicals.

##Software Requirements

* [Git](https://git-scm.com)
* [Maven](https://maven.apache.org)

##Other Requirements

* [AWS Account](https://aws.amazon.com/)
* [AWS Credentials](http://docs.aws.amazon.com/general/latest/gr/aws-security-credentials.html)

##Setup

###Code Setup

Run the following line in a terminal

    git clone git@github.com:Rosengren/ChemicalTracker.git

Go into the ChemicalTracker folder

    cd ChemicalTracker/

Build the source files by running Maven

    mvn package

To start the local server, run

    mvn spring-boot:run

The local server should be running at

    localhost:8080/ChemicalTracker/

###AWS Setup

__Note__: Only follow these steps if you are setting up a new AWS account. If you are using an
existing account with all of the services setup, you can skip this part.

1. Create an aws account [here](https://aws.amazon.com/).

2. Sign in to the console.

3. Choose the **Elastic Beanstalk** service under Compute.

4. Select "Create New Application" in the top right corner.

5. In the _Application name_ field, type: "ChemicalTracker". Adding a description is optional. Select next.

6. In the _New Environment_ page, select "Web Server Environment" and select next.

7. In the  _Predefined configuration_ dropdown, select "Tomcat".

8. In the _Environment type_ dropdown, select "Single instance" and select next.

9. On the _Application Version_ page, select "Upload your own" and select "Choose File".

10. In the file explorer pop-up, navigate to the ChemicalTracker/target/ folder that was created when you ran _mvn package_ and select the "ChemicalTracker.war" file. Then select next.

11. On the _Environment Information_ page, choose and environment name and an environment url.
__IMPORTANT:__ The "Environment URL" is the url that other applications will need to point to in
order to communicate with the server. Select next.

12. Under _Aditional Resources_, leave the options unchecked.  Select next.

13. Under _Configuration Details_, select t1.micro for "Instance type", enter your email address
in the "Email address" field. All other fields are optional. Select next.

14. Under _Environment Tags_, use you Key and Value credentials here. See section on creating
permissions.

15. Under _Permissions_, select next.

16. Under _Review Information_, make sure that everything looks good, then select Launch.

Selecting Launch, will create an EC2 (Elastic Cloud Compute) Server under the EC2 service on the
console page. If you Go to the console. select EC2, then select "Running Instances", Ensure that
there is only 1 row in the table named "chemicaltracker". If you are using aws Free-Tier, you are
allowed to run one server instance before incurring any fees. If there are more than one, select
it and choose Actions > Instance State > Terminate.
<br/>
To access the webpage, go to _Elastic Beanstalk_, select the environment you created (ie.
chemicaltacker-env) and click on the link near the top: <url>.elasticbeanstalk.com.
<br/>
If all went well, on the _Elastic Beanstalk_ page under Overview, you should see a green
checkmark and/or a line that says "Health Ok".

##Making Code Changes

1. Make a code change in the ChemicalTracker/src/ folder.

2. Open a terminal, go to Chemical/Tracker/ directory and run `mvn package`. This will re compile
the code and generate a new chemicaltracker.war file.

3. Go to Elastic Beanstalk in the AWS Console and select the chemicaltracker environment.

4. Under the Overview header, there should be a button that says: "Upload and Deploy".

5. Select this option and navigate to the chemicaltracker.war file located in the
ChemicalTracker/Tracker/ directory.

6. After a few minutes, the online version should be updated.
