Grading System Group 19
==========
### Cowokers and BUID:
    Name: Yinyun Cao    BUID:U36509179
    Name: Qichao Hong   BUID:U83816176
	Name: Yuang Liu     BUID:U99473611
	Name: Zhitong Wu    BUID:U93211428
	
### How to run:
The entrance of the program is /CONTROLLER/Entrance.java. You can run this program by

	javac BankEntrance.java
	java BankEntrance
	
### Database
    We use sqlite database.
    You do not need to create the datebase manually, the system will create it automatically.
    The JDBC is also provided. 

### User Interface
    We use fxml user interface.
    If you use Oracle Java 8.0 the supporting files are already included.
    Or you can add the support file, which are in 'lib' folder, into your IDE.
	
### Default information of the bank:
    All the default information except the password for manager could be modified by manager.
    
    The password for manager is 123456
    The max length of the password is 8
    The default saving interest is 0.05
    The default loan interest is 0.1
    The defualt sercive fee is 10 for that kind of currency
    Saving account with more than 100 will earn interests by default
    Savimg account with more than 500 will open security account by default

### Need to remember:
    Need customer's Id, name, password to login.
    Need account's Id, password to modify account.

### Some configurations: 
    The create account, close account, withdraw money operations will charge service fee to that account.
    The get transaction opearation will charge service fee to all the accounts.
    The make transaction operation will charge service fee to both accounts.
    Sell stocks or buy stocks will charge service fee from the saving account.
    
    People can only loan dollar from bank when they have two times dollar in their deposite bigger than the loan.
    
    All stocks' price is in dollar.

### Operations for manager:
##### Look all customer:
    Show the infomation of all customers
##### Look all money:
    Show all amount of money in the bank
    Account number means customer's deposite
        Positive is customer's monay in bank
        Negative is customer owe bank money
    Loan number means customer's loan
##### Look all the loan:
    Look all the loan infomation of the bank
##### Looking the customer with the most loan:
    Looking the customer with the most loan
##### Looking all stocks:
    Looking all stocks in market
##### Looking customer's stocks:
    Looking all stocks bought by customers
##### Change stock's price:
    Change stock's price
##### Change stock's available:
    Change stock's available
##### Add new stocks:
    Add new stocks
##### Delete a stock:
    Delete a stock
##### Look specific customer:
    Look specific customer by entering the Id and name
##### Change interest:
    Change the saving interest and loan interest of the bank
##### Calculate the loan interest:
    Update all the customers' loans basing on loan interest
##### Calculate the save interest:
    Update all the customers' savings basing on save interest
##### Change the service fee:
    Changing the service fee of the bank
##### Look the log:
    Look the log of the bank
##### Look bank balance
    Look the balance of the bank
##### Modify saving limit to earn interest:
    Modify saving limit to earn interest
##### Modify saving limit to create security:
    Modify saving limit to create security
##### Logout:
    return to the main menu UI


### Operations for customer:
##### Create checking:
    Create a checking accoung
    Need to enter a password within 8 length
    Account start with -10 balance (service fee)
##### Create saving:
    Create a saving accoung
    Need to enter a password within 8 length
    Account start with -10 balance (service fee)
##### Create security:
    Create a saving accoung
    Need to enter a password within 8 length
    Need to associate with a saving account
##### Buy stocks:
    Buy stocks
##### View stocks:
    View all bought stocks
##### Sell stocks:
    Sell all stocks from a company in a security accunt
##### Add money to account:
    Need to account id and password to login account
    Add money to an account
##### Withdraw money from account:
    Need to account id and password to login account
    Withdraw money from account.
##### Make transaction between account:
    Transfer money between account
    A service fee will be charged to both from account and to account.
##### View account:
    Show the information of all accounts
##### Request a loan:
    Request a loan from the bank
##### Pay for a loan:
    Pay for a loan from an account
##### View balance:
    View all the balance relating with customer
        Checking balance
        Saving balance
        Loan balance
##### Get transaction:
    Get the transaction (all operations) of the customer
##### Close account:
    Close an account with id and password
    The left money will show
        Positive number means customer get money back from bank
        Negative number maens customer should pay for bank
##### Logout:
    return to the main menu UI
##### Endday:
    End the day.



