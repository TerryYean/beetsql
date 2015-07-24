selectUser
===
    select * from user where 1=1
    @if(user.age==1){
    and age = ${user.age}
    @}
    
selectAll
===
    select * from user 