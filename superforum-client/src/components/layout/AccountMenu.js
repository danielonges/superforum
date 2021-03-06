import React, { Fragment, useContext } from 'react';
import { Link } from 'react-router-dom';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Tooltip from '@mui/material/Tooltip';
import AddCommentIcon from '@mui/icons-material/AddComment';
import PeopleIcon from '@mui/icons-material/People';
import HomeIcon from '@mui/icons-material/Home';
import Logout from '@mui/icons-material/Logout';

import UserContext from '../../context/user/userContext';

const AccountMenu = () => {
    const userContext = useContext(UserContext);

    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);

    const { user, logout } = userContext;

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const onClickLogout = (e) => {
        e.preventDefault();
        logout();
    };

    return (
        <React.Fragment>
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    textAlign: 'center'
                }}
            >
                <Tooltip title='Account settings'>
                    <IconButton
                        onClick={handleClick}
                        size='small'
                        sx={{ ml: 2 }}
                    >
                        <Avatar sx={{ width: 32, height: 32 }}>
                            {user.username.substring(0, 1)}
                        </Avatar>
                    </IconButton>
                </Tooltip>
            </Box>
            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                onClick={handleClose}
                PaperProps={{
                    elevation: 0,
                    sx: {
                        overflow: 'visible',
                        filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                        mt: 1.5,
                        '& .MuiAvatar-root': {
                            width: 32,
                            height: 32,
                            ml: -0.5,
                            mr: 1
                        },
                        '&:before': {
                            content: '""',
                            display: 'block',
                            position: 'absolute',
                            top: 0,
                            right: 14,
                            width: 10,
                            height: 10,
                            bgcolor: 'background.paper',
                            transform: 'translateY(-50%) rotate(45deg)',
                            zIndex: 0
                        }
                    }
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
                <MenuItem component={Link} to={'/profile'}>
                    <Avatar />
                    My Profile
                </MenuItem>
                <Divider />
                <MenuItem component={Link} to={'/'}>
                    <ListItemIcon>
                        <HomeIcon fontSize='small' />
                    </ListItemIcon>
                    Home Page
                </MenuItem>
                {user.userType === 'ADMIN' && (
                    <div>
                        <MenuItem component={Link} to={'/create-forum'}>
                            <ListItemIcon>
                                <AddCommentIcon fontSize='small' />
                            </ListItemIcon>
                            Create Forum
                        </MenuItem>
                        <MenuItem component={Link} to={'/manage-users'}>
                            <ListItemIcon>
                                <PeopleIcon fontSize='small' />
                            </ListItemIcon>
                            Manage Users
                        </MenuItem>
                    </div>
                )}
                <MenuItem onClick={onClickLogout}>
                    <ListItemIcon>
                        <Logout fontSize='small' />
                    </ListItemIcon>
                    Logout
                </MenuItem>
            </Menu>
        </React.Fragment>
    );
};

export default AccountMenu;
