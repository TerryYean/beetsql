selectUser
===
    select * from user where 1=1
    @if(user.age==1){
    and age = #user.age#
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