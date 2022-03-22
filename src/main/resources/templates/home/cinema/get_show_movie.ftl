<div class = "film-img flexslider_0" id="current-show-movie">
<ul class="fn-clear slides">
    <#list movieList as movie>
    <li>
        <a href="javascript:void(0);" class="li-movie" data-id="${movie.id}" data-name="${movie.name}" data-rate="${movie.rate!"0"}" data-directed-by="${movie.directedBy}" data-actor="${movie.actor}">
            <img src="/photo/view?filename=${movie.mainPic}" width="140" height="193" alt="${movie.name}" />
        </a>
        <span class="tip"></span>
    </li>
    </#list>
</ul>
</div>
<script>
$(".li-movie").click(function(){
var id = $(this).attr('data-id');
var name = $(this).attr('data-name');
var directedBy = $(this).attr('data-directed-by');
var actor = $(this).attr('data-actor');
var rate = $(this).attr('data-rate');
$("#current-movie").show();
$("#current-movie-name").text(name);
$("#current-movie-directed-by").text(directedBy);
$("#current-movie-actor").text(actor);
$("#current-movie-rate").text(rate);
$("#current-movie-rate-star").attr('data-average', rate);
$("#current-movie-href").attr('href', '/home/movie/detail?id=' + id);
$('#current-movie-rate-star').jRating({
rateMax: 10,
isDisabled: true,
bigStarsPath: '/home/images/ico_tb_stars.png'
});
    $(this).parent().siblings().find("a").removeClass('select');
    $(this).parent().siblings().find(".tip").hide();
    $(this).addClass('select');
	$(this).next(".tip").show();
	getMovieSession(id);
});


</script>
