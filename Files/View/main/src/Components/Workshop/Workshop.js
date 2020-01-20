import React from 'react';
import {GalleryCard, Grid, Page} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";

class Workshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            "imageURL": "demo/photos/grant-ritchie-338179-500.jpg",
            "avatarURL": "demo/faces/male/41.jpg",
            "fullName": "Nathan Guerrero",
            "dateString": "12 days ago",
            "totalView": 112,
            "totalLike": 42
        }
    }

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row>
                        {/*<Grid.Col sm={'12'} lg={'6'}>*/}
                            <GalleryCard>
                                <GalleryCard.Image
                                    src={this.state.imageURL}
                                    alt={`Photo by ${this.state.fullName}`}
                                />
                                <GalleryCard.Footer>
                                    <GalleryCard.Details
                                        avatarURL={this.state.avatarURL}
                                        fullName={this.state.fullName}
                                        dateString={this.state.dateString}
                                    />
                                </GalleryCard.Footer>
                            </GalleryCard>
                        {/*</Grid.Col>*/}
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Workshop;
