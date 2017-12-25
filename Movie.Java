ublic class Movie {

    public String mtitle;
    public String description;
    public String imageURL;

    Movie(String mtitle,String description,String imageURL){
        this.mtitle = mtitle;
        this.description = description;
        this.imageURL = "http://image.tmdb.org/t/p/w185/"+imageURL;
    }
}
