import { Location } from './location';

export class Element {
    elementId: string;
    type: string;
    name: string;
    active: boolean;
    createdTimestamp: Date;
    createdBy = {};
    location: Location = new Location();
    elementAttributes = {};


}
