selectUser
===
    select * from user where 1=1
    @if(user.age==1){
    and age = #user.age#
    @}
    
selectUser2
===
    select * from user where 1=1
    @if(age==12){
    and age = #age#
    @}
    
selectAll
===
    select * from user 
    
selectByExample
===
    select * from user  
    @use("example");
  
example
===  
   where 1=1 and name = #name#