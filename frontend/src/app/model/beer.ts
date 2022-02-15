export class Beer {
    id: number;
    name: string;
    description: string;
    tagline: string;
    image_url: string;

    constructor(id: number, name: string, 
        description : string, tagline : string, image_url : string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tagline = tagline;
        this.image_url = image_url;
      }
}
