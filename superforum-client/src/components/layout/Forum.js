import React, { useContext, useEffect, useState } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Sidebar from '../forum/Sidebar';
import Footer from '../forum/Footer';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';

import { withRouter } from 'react-router-dom';
import { Button } from '@mui/material';

const theme = createTheme();

const Forum = (props) => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const [popularThreads, setPopularThreads] = useState([]);
    const [openDialog, setOpenDialog] = React.useState(false);

    const { forum, setForum, deleteForum } = forumContext;
    const { user } = userContext;

    const handleClickOpen = () => {
        setOpenDialog(true);
    };

    const handleClose = (e, handleDelete) => {
        if (handleDelete) {
            deleteForum(forum.id, user.id);
            props.history.push('/');
        } else {
            setOpenDialog(false);
        }
    };

    useEffect(() => {
        setPopularThreads(
            [...forum.forumThreads]
                .sort((a, b) => b.views - a.views)
                .slice(0, 4)
        );
    }, [forum]);

    const onClickEditForum = (e) => {
        setForum(forum);
        props.history.push('/edit-forum');
    };

    const onClickNewThread = () => {
        setForum(forum);
        props.history.push('/create-thread');
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <Typography variant='h4' gutterBottom>
                    {forum.title}
                </Typography>
                <Typography variant='overline' gutterBottom>
                    {forum.forumCategory.category}
                </Typography>
                <Grid container spacing={2} columns={12} sx={{ mb: 3, mt: 1 }}>
                    <Grid item xs={2}>
                        <Button
                            variant='contained'
                            startIcon={<AddIcon />}
                            onClick={onClickNewThread}
                        >
                            New Thread
                        </Button>
                    </Grid>
                    {user.userType === 'ADMIN' && (
                        <Grid item xs={2}>
                            <Button
                                variant='outlined'
                                startIcon={<EditIcon />}
                                onClick={onClickEditForum}
                            >
                                Edit Forum
                            </Button>
                        </Grid>
                    )}
                </Grid>

                <main>
                    <Typography variant='h6' gutterBottom>
                        Popular threads
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Grid container spacing={4}>
                        {popularThreads != null && popularThreads.length > 0 ? (
                            popularThreads.map((forumThread) => (
                                <ForumThreadCard
                                    key={forumThread.id}
                                    forumThread={forumThread}
                                />
                            ))
                        ) : (
                            <Grid item xs={12}>
                                <Typography
                                    variant='subtitle1'
                                    color='text.secondary'
                                    gutterBottom
                                    sx={{ margin: 1 }}
                                >
                                    No threads in this forum!
                                </Typography>
                            </Grid>
                        )}
                    </Grid>
                    <Grid container spacing={5} sx={{ mt: 3 }}>
                        {/* include threads */}
                        <Main
                            title='All threads'
                            forums={null}
                            threads={forum.forumThreads}
                        />
                    </Grid>
                </main>
                {user.userType === 'ADMIN' && (
                    <div>
                        <Button
                            variant='outlined'
                            color='error'
                            sx={{ mt: 3, mb: 2 }}
                            onClick={handleClickOpen}
                        >
                            Delete Forum
                        </Button>
                        <Dialog
                            open={openDialog}
                            onClose={handleClose}
                            aria-labelledby='alert-dialog-title'
                            aria-describedby='alert-dialog-description'
                        >
                            <DialogTitle id='alert-dialog-title'>
                                Delete Forum
                            </DialogTitle>
                            <DialogContent>
                                <DialogContentText id='alert-dialog-description'>
                                    Are you sure you want to delete this forum?
                                </DialogContentText>
                            </DialogContent>
                            <DialogActions>
                                <Button onClick={(e) => handleClose(e, true)}>
                                    Yes
                                </Button>
                                <Button
                                    onClick={(e) => handleClose(e, false)}
                                    autoFocus
                                >
                                    No
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </div>
                )}
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};
export default Forum;
