# CS 275 Mobile Application Development I - BillBoard
BillBoard is a simple two page app with bottom navigation. 
The home page displays stats about your bills:
* What your next due bill is.
* What bills you have due within 7 days.
* What is the total amount owed across bills.

The bill-board page hosts a recycler view and lists bills.
* Bills can be created, updated, and deleted. 
	* New bills are created from the bill-board with the Add Bill option. 
	* Bills can be clicked for a detail view, where they can be edited, with edit confirmation so accidental/bad edits can be thrown away, or deleted entirely.
* Other options include showing a subtitle with the count of bills in the app and sorting the bill list by closest due date or lowest amount due. 

An issue I ran into with the app was getting the date picker to change style; I tried defining a style for it, directly setting the style, and doing it programmatically, but none of those three worked - there were always elements that did not change. 
